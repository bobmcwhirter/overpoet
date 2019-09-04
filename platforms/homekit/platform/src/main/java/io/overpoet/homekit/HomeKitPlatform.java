package io.overpoet.homekit;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import io.overpoet.spi.platform.Platform;
import io.overpoet.spi.platform.PlatformContext;
import io.overpoet.spi.ui.UI;
import io.overpoet.hap.server.HAPServer;
import io.overpoet.homekit.manipulator.HomeKitManipulator;
import io.overpoet.homekit.server.Bridge;
import io.overpoet.homekit.server.ServerStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomeKitPlatform implements Platform {

    private static final Logger LOG = LoggerFactory.getLogger("overpoet.homekit");

    public HomeKitPlatform() {

    }

    //@Override
    //public String id() {
        //return "homekit";
    //}

    //@Override
    //public String name() {
        //return "HomeKit";
    //}

    @Override
    public void initialize(PlatformContext context) {
        initializeUI(context);
        this.serverStorage = new ServerStorage(context.configuration());
        //this.bridgeAccessory = BridgeAccessoryBuilder.build();
        if (!this.serverStorage.isPaired()) {
            LOG.info("HomeKit pairing pin: {}", this.serverStorage.getPIN());
        }
        this.bridge = new Bridge();
        this.manipulator = new HomeKitManipulator(this.bridge);
        this.advertiser = new Advertiser(this.serverStorage.getPairingID());
        this.advertiser.setConfigurationNumber(this.serverStorage.getConfigurationNumber());
        this.advertiser.setIsPaired(this.serverStorage.isPaired());
        InetSocketAddress bind = new InetSocketAddress(0);
        try {
            this.hapServer = new HAPServer(this.serverStorage, this.bridge);
            int port = this.hapServer.start(bind);
            this.bridge.setUpdatedCallback(() -> {
                this.advertiser.setConfigurationNumber(this.serverStorage.incrementConfigurationNumber());
            });
            this.serverStorage.setPairingCallback(() -> {
                this.advertiser.setIsPaired(this.serverStorage.isPaired());
            });
            this.advertiser.start(port);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        context.connect(this.manipulator);
    }

    private void initializeUI(PlatformContext context) {
        UI ui = context.ui();
        ui.get( (req, resp)->{
            resp.content().writeCharSequence("homekit", Charset.forName("UTF-8"));
            resp.close();
        });
    }

    @Override
    public void stop() throws InterruptedException {
        this.advertiser.stop();
        this.hapServer.stop();
    }

    private ServerStorage serverStorage;

    private HAPServer hapServer;

    private HomeKitManipulator manipulator;

    private Bridge bridge;

    private Advertiser advertiser;
}
