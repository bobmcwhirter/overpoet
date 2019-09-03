package io.overpoet.spi.sensor;

import io.overpoet.spi.Key;
import io.overpoet.spi.chrono.TimeSlice;
import io.overpoet.spi.metadata.TimeMetadata;

public class TimeSensor extends AbstractSensor<TimeSlice, TimeMetadata> {
    public TimeSensor(Key key, TimeMetadata metadata, SensorLogic<TimeSlice> logic) {
        super(key, TimeSlice.class, metadata, logic);
    }
}
