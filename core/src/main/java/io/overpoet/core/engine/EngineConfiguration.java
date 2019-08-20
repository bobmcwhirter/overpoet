package io.overpoet.core.engine;

import io.overpoet.core.geo.Location;

public interface EngineConfiguration {
    Location location();
    ConfigurationProvider configurationProvider();
}
