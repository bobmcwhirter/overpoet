package io.overpoet.engine.engine;

import io.overpoet.spi.geo.Location;

public interface EngineConfiguration {
    Location location();
    ConfigurationProvider configurationProvider();
}
