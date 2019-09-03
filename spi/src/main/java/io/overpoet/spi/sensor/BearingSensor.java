package io.overpoet.spi.sensor;

import io.overpoet.spi.Key;
import io.overpoet.spi.measurement.Bearing;
import io.overpoet.spi.metadata.BearingMetadata;

public class BearingSensor extends AbstractSensor<Bearing, BearingMetadata> {
    public BearingSensor(Key key, BearingMetadata metadata) {
        this(key, metadata, null);
    }

    public BearingSensor(Key key, BearingMetadata metadata, SensorLogic<Bearing> logic) {
        super(key, Bearing.class, metadata, logic);
    }
}
