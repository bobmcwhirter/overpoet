package com.overpoet.core.sensor;

import com.overpoet.Key;
import com.overpoet.core.measurement.Speed;
import com.overpoet.core.metadata.SpeedMetadata;

public class SpeedSensor extends AbstractSensor<Speed, SpeedMetadata> {
    public SpeedSensor(Key key, SpeedMetadata metadata) {
        this(key, metadata, null);
    }

    public SpeedSensor(Key key, SpeedMetadata metadata, SensorLogic<Speed> logic) {
        super(key, Speed.class, metadata, logic);
    }
}
