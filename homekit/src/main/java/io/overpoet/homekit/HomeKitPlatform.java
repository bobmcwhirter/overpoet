package io.overpoet.homekit;

import io.overpoet.core.platform.Platform;
import io.overpoet.core.platform.PlatformContext;
import io.overpoet.hap.server.HAPServer;

public class HomeKitPlatform implements Platform {

    public HomeKitPlatform() {

    }

    @Override
    public void configure(PlatformContext context) {
        HAPServer server = new HAPServer(new EngineBackedAuthStorage(context.configuration()));
    }

}
