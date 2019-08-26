package io.overpoet.homekit;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import io.overpoet.core.platform.Platform;
import io.overpoet.core.platform.PlatformContext;
import io.overpoet.hap.common.model.Accessory;
import io.overpoet.hap.server.HAPServer;
import io.overpoet.hap.server.model.ServerAccessory;
import io.overpoet.homekit.server.BridgeAccessoryBuilder;
import io.overpoet.homekit.server.ServerStorage;

public class HomeKitPlatform implements Platform {

    public HomeKitPlatform() {

    }

    @Override
    public void configure(PlatformContext context) {
        this.serverStorage = new ServerStorage(context.configuration());
        this.bridgeAccessory = BridgeAccessoryBuilder.build();
        InetSocketAddress bind = new InetSocketAddress(0);
        try {
            this.hapServer = new HAPServer(this.serverStorage, this.bridgeAccessory);
            int port = this.hapServer.start(bind);
            System.err.println( "on port: " + port);
            startBonjour();
            ServiceInfo serviceInfo = register(port);
            this.serverStorage.setPairingCallback(() -> {
                serviceInfo.setText(txtRecord());
            });
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startBonjour() throws IOException {
        this.bonjour = JmDNS.create();
    }

    private ServiceInfo register(int port) throws IOException {
        ServiceInfo serviceInfo = ServiceInfo.create("_hap._tcp.",
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
        return serviceInfo;
    }

    private Map<String, ?> txtRecord() {
        Map<String, String> txt = new HashMap<>();
        txt.put("c#", "" + this.serverStorage.getConfigurationNumber());
        txt.put("ff", "0");
        txt.put("id", this.serverStorage.getPairingID());
        txt.put("md", "OverPoet HomeKit Platform");
        txt.put("pv", "1.1");
        txt.put("s#", "1");
        txt.put("sf", "" + (this.serverStorage.isPaired() ? 0 : 1));
        txt.put("ci", "2");
        return txt;
    }

    private ServerStorage serverStorage;

    private HAPServer hapServer;

    private JmDNS bonjour;

    private ServerAccessory bridgeAccessory;
}
