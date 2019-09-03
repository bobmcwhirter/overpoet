package io.overpoet.spi.geo;

import java.time.ZoneId;

import io.overpoet.spi.measurement.Distance;

public interface Location {
    Point point();
    ZoneId getZoneId();
    Distance elevation();
}
