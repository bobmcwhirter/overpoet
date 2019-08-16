package com.overpoet.core.spacetime;

import java.time.ZoneId;

public interface Location {
    Point point();
    ZoneId getZoneId();
}
