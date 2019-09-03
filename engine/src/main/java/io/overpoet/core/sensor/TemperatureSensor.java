package io.overpoet.core.sensor;

import io.overpoet.Key;
import io.overpoet.core.measurement.Temperature;
import io.overpoet.core.metadata.TemperatureMetadata;

public class TemperatureSensor extends AbstractSensor<Temperature, TemperatureMetadata> {
    public TemperatureSensor(Key key, TemperatureMetadata metadata) {
        this(key, metadata, null);
    }

    public TemperatureSensor(Key key, TemperatureMetadata metadata, SensorLogic<Temperature> logic) {
        super(key, Temperature.class, metadata, logic);
    }
}
