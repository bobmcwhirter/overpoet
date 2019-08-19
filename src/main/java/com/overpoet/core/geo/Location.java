package com.overpoet.core.geo;

import java.time.ZoneId;

import com.overpoet.core.geo.Point;

public interface Location {
    Point point();
    ZoneId getZoneId();
    Length elevation();
}
