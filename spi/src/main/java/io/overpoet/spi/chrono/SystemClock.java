package io.overpoet.spi.chrono;

import java.time.ZonedDateTime;

import io.overpoet.spi.geo.Location;

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
