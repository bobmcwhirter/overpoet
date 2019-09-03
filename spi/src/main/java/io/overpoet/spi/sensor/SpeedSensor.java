package io.overpoet.spi.sensor;

import io.overpoet.spi.Key;
import io.overpoet.spi.measurement.Speed;
import io.overpoet.spi.metadata.SpeedMetadata;

public class SpeedSensor extends AbstractSensor<Speed, SpeedMetadata> {
    public SpeedSensor(Key key, SpeedMetadata metadata) {
        this(key, metadata, null);
    }

    public SpeedSensor(Key key, SpeedMetadata metadata, SensorLogic<Speed> logic) {
        super(key, Speed.class, metadata, logic);
    }
}
