package io.overpoet.engine.solar;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.function.Function;

import io.overpoet.spi.chrono.DeltaT;
import io.overpoet.spi.geo.AzimuthZenithAngle;
import io.overpoet.spi.geo.Location;

import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.DAYS;

public class Sun {

    private static ZonedDateTime before(ZonedDateTime date, Function<ZonedDateTime, ZonedDateTime> fn) {
        return seek(date, fn, result->result.isAfter(date), of( -1, DAYS));
    }

    private static ZonedDateTime after(ZonedDateTime date, Function<ZonedDateTime, ZonedDateTime> fn) {
        return seek(date, fn, result->result.isBefore(date), of(1, DAYS));
    }

    private static ZonedDateTime seek(ZonedDateTime date, Function<ZonedDateTime, ZonedDateTime> fn, Function<ZonedDateTime, Boolean> cmp, TemporalAmount adjustment) {
        ZonedDateTime cur = date;
        ZonedDateTime result = null;
        try {
            do {
                result = fn.apply(cur);
                cur = cur.plus(adjustment);
            } while (cmp.apply(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ZonedDateTime sunriseBefore(Location location, ZonedDateTime date) {
        return before( date,
                     (d)-> SPA.calculateSunriseTransitSet(d, location.point().latitude().decimalDegrees(), location.point().longitude().decimalDegrees(), DeltaT.estimate(date))[0]);
    }

    public static ZonedDateTime sunriseAfter(Location location, ZonedDateTime date) {
        return after( date,
                     (d)-> SPA.calculateSunriseTransitSet(d, location.point().latitude().decimalDegrees(), location.point().longitude().decimalDegrees(), DeltaT.estimate(date))[0]);
    }

    public static ZonedDateTime sunsetBefore(Location location, ZonedDateTime date) {
        return before( date,
                     (d)-> SPA.calculateSunriseTransitSet(d, location.point().latitude().decimalDegrees(), location.point().longitude().decimalDegrees(), DeltaT.estimate(date))[2]);
    }

    public static ZonedDateTime sunsetAfter(Location location, ZonedDateTime date) {
        return after( date,
                     (d)-> SPA.calculateSunriseTransitSet(d, location.point().latitude().decimalDegrees(), location.point().longitude().decimalDegrees(), DeltaT.estimate(date))[2]);
    }

    public static ZonedDateTime solarNoon(Location location, ZonedDateTime date) {
        ZonedDateTime[] result = SPA.calculateSunriseTransitSet(date, location.point().latitude().decimalDegrees(), location.point().longitude().decimalDegrees(), DeltaT.estimate(date));
        return result[1];
    }

    public static AzimuthZenithAngle position(Location location, ZonedDateTime date) {
        return SPA.calculateSolarPosition(date, location.point().latitude().decimalDegrees(), location.point().longitude().decimalDegrees(), location.elevation().meters(), DeltaT.estimate(date));
    }

}
