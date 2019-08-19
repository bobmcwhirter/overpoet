package com.overpoet.core.chrono;

import java.time.ZonedDateTime;

import com.overpoet.core.geo.Location;

public interface SystemClock {
    ZonedDateTime now();

    static SystemClock real(Location location) {
        return new SystemClock() {
            @Override
            public ZonedDateTime now() {
                return ZonedDateTime.now(location.getZoneId());
            }
        };
    }
}
