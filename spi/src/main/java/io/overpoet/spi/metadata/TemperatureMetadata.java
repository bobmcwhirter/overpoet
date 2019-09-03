package io.overpoet.spi.metadata;

import io.overpoet.spi.measurement.Temperature;

public class TemperatureMetadata implements Metadata<Temperature> {

    public static final TemperatureMetadata DEFAULT = new TemperatureMetadata();

    public TemperatureMetadata() {
    }

}
