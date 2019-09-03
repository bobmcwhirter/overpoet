package io.overpoet.hap.server.codec;

import java.util.Optional;

import io.overpoet.hap.common.codec.tlv.Method;
import io.overpoet.hap.common.codec.tlv.TLV;
import io.overpoet.hap.common.codec.tlv.TLVError;
import io.overpoet.hap.common.codec.tlv.Type;
import io.overpoet.hap.common.model.value.TLV8Value;
import io.overpoet.hap.server.auth.ServerAuthStorage;

public class PairingsManager {
    public PairingsManager(ServerAuthStorage authStorage) {
        this.authStorage = authStorage;
    }

    public TLV handle(TLV in) {
        System.err.println( "add pairing: 1");
        Optional<Integer> method$ = Type.METHOD.get(in);
        if ( ! method$.isPresent() ) {
            System.err.println( "add pairing: no method present");
            return error(TLVError.UNKNOWN);
        }

        if (method$.get() == Method.ADD_PAIRING.getValue() ) {
            return handleAddPairing(in);
        } else if ( method$.get() == Method.REMOVE_PAIRING.getValue()) {
            return handleRemovePairing(in);
        } else {
            System.err.println("add pairing: method not ADD_PAIRING: " + method$.get());
        }

        return error(TLVError.UNKNOWN);
    }

    public void reset() {
        this.currentState = 0;
    }

    protected TLV handleAddPairing(TLV in) {
        Optional<Integer> state$ = Type.STATE.get(in);
        if (!state$.isPresent()) {
            System.err.println( "pair-setup: error: state not present");
            return error(TLVError.UNKNOWN);
        }

        int state = state$.get();

        if ( this.currentState == 0 && state == 1) {
            return doAddPairingResponse(in);
        }

        return error(TLVError.UNKNOWN);
    }

    protected TLV handleRemovePairing(TLV in) {
        Optional<Integer> state$ = Type.STATE.get(in);
        if (!state$.isPresent()) {
            System.err.println( "pair-setup: error: state not present");
            return error(TLVError.UNKNOWN);
        }

        int state = state$.get();

        if ( this.currentState == 0 && state == 1) {
            return doRemovePairing(in);
        }

        return error(TLVError.UNKNOWN);

    }

    protected TLV doAddPairingResponse(TLV in) {
        this.currentState = 2;

        Optional<String> identifier$ = Type.IDENTIFIER.get(in);

        if ( ! identifier$.isPresent() ) {
            System.err.println( "add-pairing: error: identifier not present");
            return error(TLVError.AUTHENTICATION);
        }

        Optional<byte[]> publicKey$ = Type.PUBLIC_KEY.get(in);

        if ( ! publicKey$.isPresent() ) {
            System.err.println( "add-pairing: error: public-key not present");
            return error(TLVError.AUTHENTICATION);
        }

        Optional<Integer> permissions$ = Type.PERMISSIONS.get(in);

        if ( ! permissions$.isPresent() ) {
            System.err.println( "add-pairing: error: permissions not present");
            return error(TLVError.AUTHENTICATION);
        }

        this.authStorage.addPairing( identifier$.get(), publicKey$.get());

        TLV out = new TLV();
        Type.STATE.set(out, this.currentState);
        return out;
    }

    protected TLV doRemovePairing(TLV in) {
        this.currentState = 2;

        Optional<String> identifier$ = Type.IDENTIFIER.get(in);

        if ( ! identifier$.isPresent() ) {
            System.err.println( "remove-pairing: error: identifier not present");
            return error(TLVError.AUTHENTICATION);
        }

        System.err.println( "removing: " + identifier$.get());

        this.authStorage.removePairing( identifier$.get() );

        TLV out = new TLV();
        Type.STATE.set(out, this.currentState);
        return out;

    }

    protected TLV error(TLVError error) {
        TLV tlv = new TLV();
        Type.STATE.set(tlv, this.currentState);
        Type.ERROR.set(tlv, error.getValue());
        return tlv;
    }

    private final ServerAuthStorage authStorage;
    private int currentState = 0;
}
