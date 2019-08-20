package io.overpoet.core.sensor;

import io.overpoet.core.chrono.TimeSlice;
import io.overpoet.core.metadata.Metadata;

public class TimeMetadata implements Metadata<TimeSlice> {

    public static final TimeMetadata INSTANCE = new TimeMetadata();

}
