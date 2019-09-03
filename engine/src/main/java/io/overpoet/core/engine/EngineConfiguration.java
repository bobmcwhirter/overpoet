package io.overpoet.core.engine;

import io.overpoet.spi.geo.Location;

public interface EngineConfiguration {
    Location location();
    ConfigurationProvider configurationProvider();
}
