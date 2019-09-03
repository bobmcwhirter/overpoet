package io.overpoet.spi.sensor;

import io.overpoet.spi.Key;
import io.overpoet.spi.metadata.IntegerMetadata;

public class IntegerSensor extends AbstractSensor<Integer, IntegerMetadata> {
    public IntegerSensor(Key key, IntegerMetadata metadata) {
        this(key, metadata, null);
    }

    public IntegerSensor(Key key, IntegerMetadata metadata, SensorLogic<Integer> logic) {
        super(key, Integer.class, metadata, logic);
    }
}
