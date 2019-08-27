package io.overpoet.core.metadata;

import io.overpoet.core.measurement.Temperature;

public class TemperatureMetadata implements Metadata<Temperature> {

    public static final TemperatureMetadata DEFAULT = new TemperatureMetadata();

    public TemperatureMetadata() {
    }

}
