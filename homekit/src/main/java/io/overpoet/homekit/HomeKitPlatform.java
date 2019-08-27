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
import io.overpoet.homekit.manipulator.HomeKitManipulator;
import io.overpoet.homekit.server.Bridge;
import io.overpoet.homekit.server.ServerStorage;

public class HomeKitPlatform implements Platform {

    public HomeKitPlatform() {

    }

    @Override
    public void initialize(PlatformContext context) {
        this.serverStorage = new ServerStorage(context.configuration());
        //this.bridgeAccessory = BridgeAccessoryBuilder.build();
        this.bridge = new Bridge();
        this.manipulator = new HomeKitManipulator(this.bridge);
        this.advertiser = new Advertiser(this.serverStorage.getPairingID());
        this.advertiser.setConfigurationNumber(this.serverStorage.getConfigurationNumber());
        this.advertiser.setIsPaired(this.serverStorage.isPaired());
        InetSocketAddress bind = new InetSocketAddress(0);
        try {
            this.hapServer = new HAPServer(this.serverStorage, this.bridge);
            int port = this.hapServer.start(bind);
            this.bridge.setUpdatedCallback( ()->{
                this.advertiser.setConfigurationNumber( this.serverStorage.incrementConfigurationNumber() );
            });
            this.serverStorage.setPairingCallback(() -> {
                this.advertiser.setIsPaired( this.serverStorage.isPaired() );
            });
            this.advertiser.start(port);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        context.connect( this.manipulator );
    }

    private ServerStorage serverStorage;
    private HAPServer hapServer;
    private HomeKitManipulator manipulator;
    private Bridge bridge;
    private Advertiser advertiser;
}
