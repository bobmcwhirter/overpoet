package io.overpoet.homekit.server;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.util.function.Function;

import io.overpoet.core.platform.PlatformConfiguration;
import io.overpoet.hap.server.auth.ServerAuthStorage;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;

public class ServerStorage implements ServerAuthStorage {

    private static final String SERVER_PREFIX = "server.";

    private static final String LTSK = configKey("ltsk");

    private static final String DEVICE_ID = configKey("device-id");

    private static final String PIN = configKey("pin");

    private static final String LTPK_PREFIX = configKey("ltpk.");

    private static String LTPK(String identifier) {
        return LTPK_PREFIX + identifier;
    }

    private static final String CONFIGURATION_NUMBER = configKey("configuration-number");

    public ServerStorage(PlatformConfiguration config) {
        this.config = config;
        initialize();
    }

    static String configKey(String name) {
        return SERVER_PREFIX + name;
    }

    private void initialize() {
        if (this.config.get(LTSK) == null) {
            EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName("ed25519-sha-512");
            byte[] seed = new byte[spec.getCurve().getField().getb() / 8];
            new SecureRandom().nextBytes(seed);
            this.config.set(LTSK, Base64.getEncoder().encodeToString(seed));
        }
        if (this.config.get(DEVICE_ID) == null) {
            byte[] deviceId = new byte[6];
            new SecureRandom().nextBytes(deviceId);
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < deviceId.length; ++i) {
                String bit = Integer.toHexString(deviceId[i]);
                if (bit.length() == 1) {
                    str.append("0");
                } else {
                    bit = bit.substring(bit.length() - 2);
                }
                str.append(bit);
                if (i + 1 != deviceId.length) {
                    str.append(":");
                }
            }
            this.config.set(DEVICE_ID, str.toString());
        }
        if (this.config.get(PIN) == null) {
            StringBuilder str = new StringBuilder();
            Random random = new Random();
            str.append(random.nextInt(10));
            str.append(random.nextInt(10));
            str.append(random.nextInt(10));
            str.append("-");
            str.append(random.nextInt(10));
            str.append(random.nextInt(10));
            str.append("-");
            str.append(random.nextInt(10));
            str.append(random.nextInt(10));
            str.append(random.nextInt(10));
            this.config.set(PIN, str.toString());
        }
        if (this.config.get(CONFIGURATION_NUMBER) == null) {
            this.config.set(CONFIGURATION_NUMBER, "1");
        }
    }

    @Override
    public byte[] getLTSK() {
        return Base64.getDecoder().decode(this.config.get(LTSK));
    }

    @Override
    public String getPairingID() {
        return this.config.get(DEVICE_ID);
    }

    @Override
    public String getPIN() {
        return this.config.get(PIN);
    }

    @Override
    public void addPairing(String identifier, byte[] ltpk) {
        boolean previouslyPaired = isPaired();
        this.config.set(LTPK(identifier), Base64.getEncoder().encodeToString(ltpk));
        this.config.set(CONFIGURATION_NUMBER, "" + (Integer.parseInt(this.config.get(CONFIGURATION_NUMBER))+1));
        if ( ! previouslyPaired ) {
            this.callback.run();
        }
    }

    @Override
    public void removePairing(String identifier) {
        this.config.remove(LTPK(identifier));
    }

    public void setPairingCallback(Runnable callback) {
        this.callback = callback;
    }

    @Override
    public byte[] getPairedLTPK(String identifier) {
        return Base64.getDecoder().decode(this.config.get(LTPK(identifier)));
    }

    public boolean isPaired() {
        return this.config.keys().stream().filter(e -> e.startsWith(LTPK_PREFIX)).findFirst().isPresent();
    }

    public int getConfigurationNumber() {
        return Integer.parseInt(this.config.get(CONFIGURATION_NUMBER));
    }

    public int incrementConfigurationNumber() {
        int num = getConfigurationNumber() + 1;
        this.config.set(CONFIGURATION_NUMBER, "" + num);
        return num;
    }

    private final PlatformConfiguration config;

    private Runnable callback;
}
