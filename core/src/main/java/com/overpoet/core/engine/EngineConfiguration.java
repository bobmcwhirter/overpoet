package com.overpoet.core.engine;

import com.overpoet.core.geo.Location;

public interface EngineConfiguration {
    Location location();
    ConfigurationProvider configurationProvider();
}
