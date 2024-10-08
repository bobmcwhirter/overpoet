package io.overpoet.hap.server.codec;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Optional;

import com.nimbusds.srp6.SRP6CryptoParams;
import com.nimbusds.srp6.SRP6Exception;
import com.nimbusds.srp6.SRP6Routines;
import com.nimbusds.srp6.SRP6ServerSession;
import com.nimbusds.srp6.SRP6Session;
import com.nimbusds.srp6.SRP6VerifierGenerator;
import com.nimbusds.srp6.XRoutineWithUserIdentity;
import io.overpoet.hap.common.codec.ByteUtils;
import io.overpoet.hap.common.codec.crypto.Chacha;
import io.overpoet.hap.common.codec.crypto.EdsaSigner;
import io.overpoet.hap.common.codec.crypto.EdsaVerifier;
import io.overpoet.hap.common.codec.pair.PairSetupManager;
import io.overpoet.hap.common.codec.tlv.TLV;
import io.overpoet.hap.common.codec.tlv.TLVError;
import io.overpoet.hap.common.codec.tlv.Type;
import io.overpoet.hap.common.codec.srp.ClientEvidenceRoutineImpl;
import io.overpoet.hap.common.codec.srp.ServerEvidenceRoutineImpl;
import io.overpoet.hap.common.codec.srp.Util;
import io.overpoet.hap.server.auth.ServerAuthStorage;

/**
 * Created by bob on 8/29/18.
 */
public class ServerPairSetupManager extends PairSetupManager<ServerAuthStorage> {

    public ServerPairSetupManager(ServerAuthStorage authStorage) {
        super(authStorage);
    }

    public void reset() {
        super.reset();
        this.session = null;
    }

    @Override
    public TLV handle(TLV in) {
        Optional<Integer> state$ = Type.STATE.get(in);
        if (!state$.isPresent()) {
            System.err.println( "pair-setup: error: state not present");
            return error(TLVError.UNKNOWN);
        }

        int state = state$.get();

        if (this.currentState == 0 && state == 1) {
            return doSRPStartResponse(in);
        }
        if (this.currentState == 2 && state == 3) {
            return doSRPVerifyResponse(in);
        }
        if (this.currentState == 4 && state == 5) {
            return doExchangeResponse(in);
        }

        return error(TLVError.UNKNOWN);
    }

    private TLV doSRPStartResponse(TLV in) {
        System.err.println( "pair-setup: attempting 1->2");
        this.currentState = 2;

        if ( getAuthStorage().isPaired()) {
            TLV out = new TLV();
            Type.STATE.set(out, this.currentState);
            Type.ERROR.set(out, TLVError.UNAVAILABLE.getValue());
            return out;
        }

        SRP6CryptoParams config = new SRP6CryptoParams(N_3072, G, "SHA-512");
        this.session = new SRP6ServerSession(config);
        this.session.setClientEvidenceRoutine(new ClientEvidenceRoutineImpl());
        this.session.setServerEvidenceRoutine(new ServerEvidenceRoutineImpl());

        SRP6VerifierGenerator verifierGenerator = new SRP6VerifierGenerator(config);
        byte[] salt = new SRP6Routines().generateRandomSalt(16);

        verifierGenerator.setXRoutine(new XRoutineWithUserIdentity());
        BigInteger verifier = verifierGenerator.generateVerifier(salt,
                                                                 IDENTIFIER_BYTES,
                                                                 getAuthStorage().getPIN().getBytes(StandardCharsets.UTF_8));


        BigInteger publicKey = this.session.step1(IDENTIFIER, new BigInteger(1, salt), verifier);
        TLV out = new TLV();
        Type.PUBLIC_KEY.set(out, Util.bigIntegerToUnsignedByteArray(publicKey));
        Type.STATE.set(out, this.currentState);
        Type.SALT.set(out, salt);
        return out;
    }

    private TLV doSRPVerifyResponse(TLV in) {
        System.err.println( "pair-setup: attempting 3->4");
        this.currentState = 4;
        Optional<byte[]> publicKey$ = Type.PUBLIC_KEY.get(in);
        if (!publicKey$.isPresent()) {
            System.err.println( "pair-setup: error: public-key not present");
            return error(this.currentState, TLVError.AUTHENTICATION);
        }
        Optional<byte[]> proof$ = Type.PROOF.get(in);
        if (!proof$.isPresent()) {
            System.err.println( "pair-setup: error: proof not present");
            return error(this.currentState, TLVError.AUTHENTICATION);
        }

        try {
            BigInteger result = this.session.step2(
                    new BigInteger(1, publicKey$.get()),
                    new BigInteger(1, proof$.get())
            );

            TLV out = new TLV();
            Type.STATE.set(out, this.currentState);
            Type.PROOF.set(out, ByteUtils.toByteArray(result));
            return out;
        } catch (SRP6Exception e) {
            System.err.println( "pair-setup: error: crypto error: " + e.getMessage());
            return error(this.currentState, TLVError.AUTHENTICATION);
        }
    }

    private TLV doExchangeResponse(TLV in) {
        System.err.println( "pair-setup: attempting 5->6");
        this.currentState = 6;

        Optional<byte[]> encryptedData$ = Type.ENCRYPTED_DATA.get(in);
        if (!encryptedData$.isPresent()) {
            System.err.println( "pair-setup: error: encrypted-data not present");
            return error(this.currentState, TLVError.AUTHENTICATION);
        }

        Chacha chacha = new Chacha(getSessionKey(getSRPSessionKey(),
                                                 PAIR_SETUP_ENCRYPT_SALT,
                                                 PAIR_SETUP_ENCRYPT_INFO) );

        Chacha.Decoder decoder = chacha.newDecoder("PS-Msg05");
        TLV subTlv = null;
        try {
            subTlv = TLV.decodeFrom(decoder.decodeEncryptedData(encryptedData$.get()) );
        } catch (IOException e) {
            System.err.println( "pair-setup: error: I/O error: " + e.getMessage());
            return error(this.currentState, TLVError.AUTHENTICATION);
        }

        Optional<String> iOSDevicePairingID$ = Type.IDENTIFIER.get(subTlv);
        Optional<byte[]> iOSDeviceLTPK$ = Type.PUBLIC_KEY.get(subTlv);
        Optional<byte[]> iOSDeviceSignature$ = Type.SIGNATURE.get(subTlv);

        byte[] sharedSecret = getSRPSessionKey();

        byte[] iOSDeviceX = getSessionKey(getSRPSessionKey(),
                                          PAIR_SETUP_CONTROLLER_SALT,
                                          PAIR_SETUP_CONTROLLER_INFO);

        byte[] iOSDeviceInfo = getIOSDeviceInfo(iOSDeviceX, iOSDevicePairingID$.get(), iOSDeviceLTPK$.get());

        EdsaVerifier verifier = new EdsaVerifier(iOSDeviceLTPK$.get());

        try {
            if (!verifier.verify(iOSDeviceInfo, iOSDeviceSignature$.get())) {
                System.err.println( "pair-setup: error: verification failed");
                return error(this.currentState, TLVError.AUTHENTICATION);
            }
        } catch (Exception e) {
            System.err.println( "pair-setup: error: verification failed: " + e.getMessage());
            return error(this.currentState, TLVError.AUTHENTICATION);
        }

        getAuthStorage().addPairing(iOSDevicePairingID$.get(), iOSDeviceLTPK$.get());

        // -- response

        EdsaSigner signer = new EdsaSigner(getAuthStorage().getLTSK());

        byte[] accessoryX = getSessionKey(sharedSecret,
                                          PAIR_SETUP_ACCESSORY_SALT,
                                          PAIR_SETUP_ACCESSORY_INFO);

        byte[] accessoryInfo = getAccessoryInfo(accessoryX,
                                                getAuthStorage().getPairingID(),
                                                signer.getPublicKey());

        try {
            byte[] signature = signer.sign(accessoryInfo);

            TLV responseSubtlv = new TLV();

            Type.IDENTIFIER.set(responseSubtlv, getAuthStorage().getPairingID());
            Type.PUBLIC_KEY.set(responseSubtlv, signer.getPublicKey());
            Type.SIGNATURE.set(responseSubtlv, signature);

            Chacha.Encoder encoder = chacha.newEncoder("PS-Msg06");

            byte[] encryptedData = encoder.encodeCiphertext(responseSubtlv.encode());

            TLV out = new TLV();
            Type.STATE.set(out, 6);
            Type.ENCRYPTED_DATA.set(out, encryptedData);
            return out;
        } catch (IOException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            System.err.println( "pair-setup: error: crypto error: " + e.getMessage());
            return error(this.currentState, TLVError.AUTHENTICATION);
        }
    }



    @Override
    protected SRP6Session getSRPSession() {
        return this.session;
    }

    private SRP6ServerSession session;
}
