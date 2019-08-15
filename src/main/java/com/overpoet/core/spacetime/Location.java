package com.overpoet.core.spacetime;

import java.time.LocalDateTime;

public interface Location {
    LocalDateTime sunriseOnOrBefore(TimeSlice target);
    LocalDateTime sunriseOnOrAfter(TimeSlice target);

    LocalDateTime sunsetOnOrBefore(TimeSlice target);
    LocalDateTime sunsetOnOrAfter(TimeSlice target);
}
