package io.overpoet.hap.server.auth;

import io.overpoet.hap.common.spi.AuthStorage;

/**
 * Created by bob on 8/29/18.
 */
public interface ServerAuthStorage extends AuthStorage {

    String getPairingID();
    String getPIN();

    void addPairing(String identifier, byte[] ltpk);
    byte[] getPairedLTPK(String identifier);
}
