package io.overpoet.engine.engine;

import io.overpoet.spi.platform.Platform;
import io.overpoet.spi.platform.PlatformConfiguration;

public interface ConfigurationProvider {
    PlatformConfiguration forPlatform(Platform platform);
}
