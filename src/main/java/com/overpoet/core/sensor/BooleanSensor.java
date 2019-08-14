package com.overpoet.core.sensor;

import com.overpoet.Key;
import com.overpoet.core.metadata.BooleanMetadata;

public class BooleanSensor extends AbstractSensor<Boolean, BooleanMetadata> {
    public BooleanSensor(Key key, BooleanMetadata metadata) {
        this(key, metadata, null);
    }

    public BooleanSensor(Key key, BooleanMetadata metadata, SensorLogic<Boolean> logic) {
        super(key, Boolean.class, metadata, logic);
    }
}
