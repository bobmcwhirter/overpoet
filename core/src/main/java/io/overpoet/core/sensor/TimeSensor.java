package io.overpoet.core.sensor;

import io.overpoet.Key;
import io.overpoet.core.chrono.TimeSlice;
import io.overpoet.core.metadata.TimeMetadata;

public class TimeSensor extends AbstractSensor<TimeSlice, TimeMetadata> {
    public TimeSensor(Key key, TimeMetadata metadata, SensorLogic<TimeSlice> logic) {
        super(key, TimeSlice.class, metadata, logic);
    }
}
