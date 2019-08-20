package com.overpoet.core.sensor;

import com.overpoet.Key;
import com.overpoet.core.measurement.Bearing;
import com.overpoet.core.metadata.BearingMetadata;
import com.overpoet.core.metadata.IntegerMetadata;

public class BearingSensor extends AbstractSensor<Bearing, BearingMetadata> {
    public BearingSensor(Key key, BearingMetadata metadata) {
        this(key, metadata, null);
    }

    public BearingSensor(Key key, BearingMetadata metadata, SensorLogic<Bearing> logic) {
        super(key, Bearing.class, metadata, logic);
    }
}
