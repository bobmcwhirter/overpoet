package io.overpoet.core.engine;

import java.util.Properties;

import io.overpoet.core.platform.Platform;
import io.overpoet.core.platform.PlatformConfiguration;

public interface ConfigurationProvider {
    PlatformConfiguration forPlatform(Platform platform);
}
