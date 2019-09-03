package io.overpoet.engine.solar;

import java.time.Duration;
import java.time.ZonedDateTime;

import io.overpoet.spi.chrono.DeltaT;
import io.overpoet.spi.chrono.SystemClock;
import io.overpoet.spi.geo.Location;
import io.overpoet.spi.sensor.TimerDrivenSensorLogic;

import static java.time.temporal.ChronoUnit.MINUTES;

public class SunElevationSensorLogic extends TimerDrivenSensorLogic<Double> {

    public SunElevationSensorLogic(SystemClock clock, Location location) {
        super(clock, Duration.of(10, MINUTES));
        this.location = location;
    }

    @Override
    protected void drive(ZonedDateTime now) {
        sink( SPA.calculateSolarPosition(now,
                                         this.location.point().latitude().decimalDegrees(),
                                         this.location.point().longitude().decimalDegrees(),
                                         this.location.elevation().meters(),
                                         DeltaT.estimate(now )).getZenithAngle() - 90.0);
    }

    private final Location location;
}
