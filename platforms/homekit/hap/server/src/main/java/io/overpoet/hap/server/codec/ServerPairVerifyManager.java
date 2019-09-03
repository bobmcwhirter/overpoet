package io.overpoet.hap.server.codec;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.util.Optional;

import javax.jmdns.impl.JmDNSImpl;

import djb.Curve25519;
import io.overpoet.hap.common.codec.crypto.Chacha;
import io.overpoet.hap.common.codec.crypto.EdsaSigner;
import io.overpoet.hap.common.codec.crypto.EdsaVerifier;
import io.overpoet.hap.common.codec.tlv.TLV;
import io.overpoet.hap.common.codec.tlv.TLVError;
import io.overpoet.hap.common.codec.tlv.Type;
import io.overpoet.hap.common.codec.pair.PairVerifyManager;
import io.overpoet.hap.server.auth.ServerAuthStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bob on 9/10/18.
 */
public class ServerPairVerifyManager extends PairVerifyManager<ServerAuthStorage> {
    private static Logger LOG = LoggerFactory.getLogger("overpoet.hap.server");

    public ServerPairVerifyManager(ServerAuthStorage authStorage) {
        super(authStorage);
    }

    public TLV handle(TLV in) {
        Optional<Integer> state$ = Type.STATE.get(in);
        if (!state$.isPresent()) {
            return error(TLVError.UNKNOWN);
        }

        int state = state$.get();

        if (this.currentState == 0 && state == 1) {
            return doVerifyStartResponse(in);
        } else if (this.currentState == 2 && state == 3) {
            return doVerifyFinishResponse(in);
        }

        return error(TLVError.UNKNOWN);
    }

    private TLV doVerifyStartResponse(TLV in) {
        LOG.debug("attempting transition 1>2");
        this.currentState = 2;

        this.curvePublicKey = new byte[32];
        this.curveSecretKey = new byte[32];

        new SecureRandom().nextBytes(this.curveSecretKey);
        Curve25519.keygen(this.curvePublicKey, null, this.curveSecretKey);

        Optional<byte[]> publicKey$ = Type.PUBLIC_KEY.get(in);

        if (!publicKey$.isPresent()) {
            LOG.error("pair-verify: error: public-key not present");
            return error(this.currentState, TLVError.AUTHENTICATION);
        }

        this.otherCurvePublicKey = publicKey$.get();

        byte[] sharedSecret = getSharedSecret(this.curveSecretKey, this.otherCurvePublicKey);

        byte[] accessoryInfo = getAccessoryInfo(this.curvePublicKey, getAuthStorage().getPairingID(), publicKey$.get());

        EdsaSigner signer = new EdsaSigner(getAuthStorage().getLTSK());
        byte[] signature = new byte[0];
        try {
            signature = signer.sign(accessoryInfo);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            LOG.error("pair-verify: error: crypto error: " + e.getMessage());
            return error(TLVError.UNKNOWN);
        }

        TLV subTlv = new TLV();
        Type.IDENTIFIER.set(subTlv, getAuthStorage().getPairingID());
        Type.SIGNATURE.set(subTlv, signature);

        this.sessionKey = getSessionKey(sharedSecret, PAIR_VERIFY_ENCRYPT_SALT, PAIR_VERIFY_ENCRYPT_INFO);

        Chacha chacha = new Chacha(this.sessionKey);
        Chacha.Encoder encoder = chacha.newEncoder("PV-Msg02");
        byte[] encryptedData = new byte[0];
        try {
            encryptedData = encoder.encodeCiphertext(subTlv.encode());
        } catch (IOException e) {
            LOG.error("pair-verify: error: I/O error: " + e.getMessage());
            return error(TLVError.UNKNOWN);
        }

        TLV out = new TLV();
        Type.STATE.set(out, this.currentState);
        Type.PUBLIC_KEY.set(out, this.curvePublicKey);
        Type.ENCRYPTED_DATA.set(out, encryptedData);
        return out;
    }

    private TLV doVerifyFinishResponse(TLV in) {
        LOG.debug("attempting transition 3>4");
        this.currentState = 4;
        Optional<byte[]> encryptedData$ = Type.ENCRYPTED_DATA.get(in);
        if (!encryptedData$.isPresent()) {
            LOG.error("pair-verify: error: encrypted data not present");
            return error(TLVError.AUTHENTICATION);
        }
        Chacha chacha = new Chacha(this.sessionKey);
        Chacha.Decoder decoder = chacha.newDecoder("PV-Msg03");
        TLV subTlv = null;
        try {
            subTlv = TLV.decodeFrom(decoder.decodeEncryptedData(encryptedData$.get()));
        } catch (IOException e) {
            LOG.error("pair-verify: error: I/O error: " + e.getMessage());
            return error(TLVError.AUTHENTICATION);
        }

        Optional<String> identifier$ = Type.IDENTIFIER.get(subTlv);
        if (!identifier$.isPresent()) {
            LOG.error("pair-verify: error: identifier not present");
            return error(TLVError.AUTHENTICATION);
        }
        Optional<byte[]> signature$ = Type.SIGNATURE.get(subTlv);
        if (!signature$.isPresent()) {
            LOG.error("pair-verify: error: signature not present");
            return error(TLVError.AUTHENTICATION);
        }

        byte[] ltpk = getAuthStorage().getPairedLTPK(identifier$.get());

        byte[] iosDeviceInfo = getIOSDeviceInfo(this.otherCurvePublicKey, identifier$.get(), this.curvePublicKey);

        EdsaVerifier verifier = new EdsaVerifier(ltpk);
        try {
            if (!verifier.verify(iosDeviceInfo, signature$.get())) {
                LOG.error("pair-verify: error: verification failed");
                return error(TLVError.AUTHENTICATION);
            }
        } catch (Exception e) {
            LOG.error("pair-verify: error: verification failed: " + e.getMessage());
            return error(TLVError.AUTHENTICATION);
        }

        TLV out = new TLV();
        Type.STATE.set(out, this.currentState);

        LOG.debug("pair-verify success");
        setKeysForSharedSecret(this.curveSecretKey, this.otherCurvePublicKey);

        return out;
    }

    public void reset() {
        super.reset();
        this.currentState = 0;
        this.curvePublicKey = null;
        this.curveSecretKey = null;
    }

    private byte[] curvePublicKey;

    private byte[] curveSecretKey;

    private byte[] sessionKey;

    private byte[] otherCurvePublicKey;
}
