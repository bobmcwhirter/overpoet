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
        LOG.info("Initialize 1");
        this.serverStorage = new ServerStorage(context.configuration());
        //this.bridgeAccessory = BridgeAccessoryBuilder.build();
        if (!this.serverStorage.isPaired()) {
            LOG.info("HomeKit pairing pin: {}", this.serverStorage.getPIN());
        }
        LOG.info("Initialize 2");
        this.bridge = new Bridge();
        this.manipulator = new HomeKitManipulator(this.bridge);
        this.advertiser = new Advertiser(this.serverStorage.getPairingID());
        this.advertiser.setConfigurationNumber(this.serverStorage.getConfigurationNumber());
        this.advertiser.setIsPaired(this.serverStorage.isPaired());
        LOG.info("Initialize 3");
        InetSocketAddress bind = new InetSocketAddress(0);
        try {
            LOG.info("Initialize 4");
            this.hapServer = new HAPServer(this.serverStorage, this.bridge);
            int port = this.hapServer.start(bind);
            LOG.info("Initialize 5");
            this.bridge.setUpdatedCallback(() -> {
                LOG.info("Initialize 6");
                this.advertiser.setConfigurationNumber(this.serverStorage.incrementConfigurationNumber());
            });
            LOG.info("Initialize 7");
            this.serverStorage.setPairingCallback(() -> {
                this.advertiser.setIsPaired(this.serverStorage.isPaired());
            });
            LOG.info("Initialize 8");
            this.advertiser.start(port);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        LOG.info("Initialize 9");
        context.connect(this.manipulator);
        LOG.info("Initialize 10");
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
