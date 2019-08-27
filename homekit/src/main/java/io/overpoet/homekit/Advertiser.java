package io.overpoet.homekit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

public class Advertiser {

    public Advertiser(String pairingID) {
        this.pairingID = pairingID;
    }

    public void start(int port) throws IOException {
        this.bonjour = JmDNS.create();
        this.serviceInfo = ServiceInfo.create("_hap._tcp.",
                                              "OverPoet HomeKit Platform",
                                              port,
                                              0,
                                              0,
                                              false,
                                              txtRecord());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.bonjour.unregisterService(serviceInfo);
        }));
        this.bonjour.registerService(serviceInfo);
    }

    private Map<String, ?> txtRecord() {
        Map<String, String> txt = new HashMap<>();
        txt.put("c#", "" + this.configurationNumber);
        txt.put("ff", "0");
        txt.put("id", this.pairingID);
        txt.put("md", "OverPoet HomeKit Platform");
        txt.put("pv", "1.1");
        txt.put("s#", "1");
        txt.put("sf", "" + (this.isPaired ? 0 : 1));
        txt.put("ci", "2");
        return txt;
    }


    public void stop() {
        this.bonjour.unregisterService(this.serviceInfo);
    }

    public void setIsPaired(boolean isPaired) {
        this.isPaired = isPaired;
        updateAdvertisement();
    }

    public void setConfigurationNumber(int configurationNumber) {
        this.configurationNumber = configurationNumber;
        updateAdvertisement();
    }

    public void updateAdvertisement() {
        if ( this.serviceInfo != null ) {
            this.serviceInfo.setText(txtRecord());
        }
    }

    private JmDNS bonjour;

    private ServiceInfo serviceInfo;

    private final String pairingID;

    private boolean isPaired = true;

    private int configurationNumber = 1;
}
