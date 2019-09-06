package io.overpoet.spi.sensor;

import io.overpoet.spi.Key;
import io.overpoet.spi.metadata.IntegerMetadata;

public class PercentageSensor extends AbstractSensor<Integer, IntegerMetadata> {
    public PercentageSensor(Key key, IntegerMetadata metadata) {
        this(key, metadata, null);
    }

    public PercentageSensor(Key key, IntegerMetadata metadata, SensorLogic<Integer> logic) {
        super(key, Integer.class, metadata, logic);
    }
}
