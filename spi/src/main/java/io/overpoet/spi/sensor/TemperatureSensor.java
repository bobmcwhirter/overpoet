package io.overpoet.spi.sensor;

import io.overpoet.spi.Key;
import io.overpoet.spi.measurement.Temperature;
import io.overpoet.spi.metadata.TemperatureMetadata;

public class TemperatureSensor extends AbstractSensor<Temperature, TemperatureMetadata> {
    public TemperatureSensor(Key key, TemperatureMetadata metadata) {
        this(key, metadata, null);
    }

    public TemperatureSensor(Key key, TemperatureMetadata metadata, SensorLogic<Temperature> logic) {
        super(key, Temperature.class, metadata, logic);
    }
}
