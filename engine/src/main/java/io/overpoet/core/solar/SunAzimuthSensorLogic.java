package io.overpoet.core.solar;

import java.time.Duration;
import java.time.ZonedDateTime;

import io.overpoet.core.chrono.DeltaT;
import io.overpoet.core.chrono.SystemClock;
import io.overpoet.core.geo.Location;
import io.overpoet.core.sensor.TimerDrivenSensorLogic;

import static java.time.temporal.ChronoUnit.MINUTES;

public class SunAzimuthSensorLogic extends TimerDrivenSensorLogic<Double> {

    public SunAzimuthSensorLogic(SystemClock clock, Location location) {
        super(clock, Duration.of(10, MINUTES));
        this.location = location;
    }

    @Override
    protected void drive(ZonedDateTime now) {
        sink( SPA.calculateSolarPosition(now,
                                         this.location.point().latitude().decimalDegrees(),
                                         this.location.point().longitude().decimalDegrees(),
                                         this.location.elevation().meters(),
                                         DeltaT.estimate(now )).getAzimuth());
    }

    private final Location location;
}
