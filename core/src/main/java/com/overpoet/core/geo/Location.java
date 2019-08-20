package com.overpoet.core.geo;

import java.time.ZoneId;

import com.overpoet.core.measurement.Distance;

public interface Location {
    Point point();
    ZoneId getZoneId();
    Distance elevation();
}
