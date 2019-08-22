package io.overpoet.homekit;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import io.overpoet.core.platform.Platform;
import io.overpoet.core.platform.PlatformContext;
import io.overpoet.hap.server.HAPServer;
import io.overpoet.homekit.server.ServerStorage;

public class HomeKitPlatform implements Platform {

    public HomeKitPlatform() {

    }

    @Override
    public void configure(PlatformContext context) {
        this.serverStorage = new ServerStorage(context.configuration());
        InetSocketAddress bind = new InetSocketAddress(0);
        try {
            this.hapServer = new HAPServer(this.serverStorage);
            int port = this.hapServer.start(bind);
            startBonjour();
            register(port);
            this.serverStorage.setPairingCallback( ()->{
                this.bonjour.unregisterAllServices();
                try {
                    register(port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startBonjour() throws IOException {
        this.bonjour = JmDNS.create();
    }

    private void register(int port) throws IOException {
        ServiceInfo serviceInfo = ServiceInfo.create("_hap._tcp.",
                                                     "OverPoet HomeKit Platform",
                                                     port,
                                                     0,
                                                     0,
                                                     false,
                                                     txtRecord());
        this.bonjour.registerService(serviceInfo);
    }

    private Map<String, ?> txtRecord() {
        Map<String, String> txt = new HashMap<>();
        txt.put("c#", "" + this.serverStorage.getConfigurationNumber());
        txt.put("ff", "0");
        txt.put("id", this.serverStorage.getPairingID());
        txt.put("md", "OverPoet HomeKit Platform");
        txt.put("pv", "1.0");
        txt.put("s#", "1");
        txt.put("sf", "" + (this.serverStorage.isPaired() ? 0 : 1));
        txt.put("ci", "2");
        return txt;
    }

    private ServerStorage serverStorage;
    private HAPServer hapServer;
    private JmDNS bonjour;

}
