package io.overpoet.core.engine;

import io.overpoet.spi.geo.Location;

public class SimpleEngineConfiguration implements EngineConfiguration {

    public SimpleEngineConfiguration(Location location, ConfigurationProvider configurationProvider) {
        this.location = location;
        this.configurationProvider = configurationProvider;
    }
    @Override
    public Location location() {
        return this.location;
    }

    @Override
    public ConfigurationProvider configurationProvider() {
        return this.configurationProvider;
    }

    private final Location location;

    private final ConfigurationProvider configurationProvider;
}
