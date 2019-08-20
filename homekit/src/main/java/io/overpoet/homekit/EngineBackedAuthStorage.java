package io.overpoet.homekit;

import java.util.Base64;
import java.util.Properties;

import io.overpoet.core.platform.PlatformConfiguration;
import io.overpoet.hap.server.auth.ServerAuthStorage;

public class EngineBackedAuthStorage implements ServerAuthStorage {

    public EngineBackedAuthStorage(PlatformConfiguration config) {
        this.config = config;
    }

    @Override
    public byte[] getLTSK() {
        return Base64.getDecoder().decode( this.config.get("ltsk"));
    }

    @Override
    public String getPairingID() {
        return "overpoet-homekit";
    }

    @Override
    public String getPIN() {
        return this.config.get("pin");
    }

    @Override
    public void addPairing(String identifier, byte[] ltpk) {

    }

    @Override
    public byte[] getPairedLTPK(String identifier) {
        return new byte[0];
    }

    private final PlatformConfiguration config;
}
