package com.overpoet.core.sensor;

import com.overpoet.core.chrono.TimeSlice;
import com.overpoet.core.metadata.Metadata;

public class TimeMetadata implements Metadata<TimeSlice> {

    public static final TimeMetadata INSTANCE = new TimeMetadata();

}
