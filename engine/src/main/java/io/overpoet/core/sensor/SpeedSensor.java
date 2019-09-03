package io.overpoet.core.sensor;

import io.overpoet.Key;
import io.overpoet.core.measurement.Speed;
import io.overpoet.core.metadata.SpeedMetadata;

public class SpeedSensor extends AbstractSensor<Speed, SpeedMetadata> {
    public SpeedSensor(Key key, SpeedMetadata metadata) {
        this(key, metadata, null);
    }

    public SpeedSensor(Key key, SpeedMetadata metadata, SensorLogic<Speed> logic) {
        super(key, Speed.class, metadata, logic);
    }
}
