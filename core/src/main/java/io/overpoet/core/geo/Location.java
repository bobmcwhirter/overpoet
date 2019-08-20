package io.overpoet.core.geo;

import java.time.ZoneId;

import io.overpoet.core.measurement.Distance;

public interface Location {
    Point point();
    ZoneId getZoneId();
    Distance elevation();
}
