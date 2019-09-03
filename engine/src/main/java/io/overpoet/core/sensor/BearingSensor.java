package io.overpoet.core.sensor;

import io.overpoet.Key;
import io.overpoet.core.measurement.Bearing;
import io.overpoet.core.metadata.BearingMetadata;

public class BearingSensor extends AbstractSensor<Bearing, BearingMetadata> {
    public BearingSensor(Key key, BearingMetadata metadata) {
        this(key, metadata, null);
    }

    public BearingSensor(Key key, BearingMetadata metadata, SensorLogic<Bearing> logic) {
        super(key, Bearing.class, metadata, logic);
    }
}
