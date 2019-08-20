package com.overpoet.core.sensor;

import com.overpoet.Key;
import com.overpoet.core.chrono.TimeSlice;

public class TimeSensor extends AbstractSensor<TimeSlice, TimeMetadata> {
    public TimeSensor(Key key, TimeMetadata metadata, SensorLogic<TimeSlice> logic) {
        super(key, TimeSlice.class, metadata, logic);
    }
}
