package io.overpoet.automation.rule;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalUnit;
import java.util.function.Function;

import io.overpoet.spi.Key;
import io.overpoet.spi.sensor.Sensor;
import io.overpoet.spi.geo.Location;
import io.overpoet.spi.chrono.TimeSlice;
import io.overpoet.automation.solar.Sun;

public class TimeCondition extends SensorCondition<TimeSlice> {
    public TimeCondition(Key clockKey, Function<TimeSlice, Boolean> condition) {
        super(clockKey, condition);
    }

    public static TimeCondition atSunrise(Key clockKey, Location location) {
        return new TimeCondition(clockKey, (t)-> {
            ZonedDateTime sunrise = Sun.sunriseBefore(location, t.now());
            return t.includes(sunrise);
        });
    }

    public static TimeCondition beforeSunrise(Key clockKey, Location location, long amount, TemporalUnit units) {
        return new TimeCondition(clockKey, (t)-> {
            ZonedDateTime sunrise = Sun.sunriseAfter(location, t.lastTick());
            sunrise = sunrise.minus(amount, units);
            return t.includes(sunrise);
        });
    }

    public static TimeCondition afterSunrise(Key clockKey, Location location, long amount, TemporalUnit units) {
        return new TimeCondition(clockKey, (t)-> {
            ZonedDateTime sunrise = Sun.sunriseBefore(location, t.lastTick());
            return t.includes(sunrise);
        });
    }

    public static TimeCondition atSunset(Key clockKey, Location location) {
        return new TimeCondition(clockKey, (t)-> {
            ZonedDateTime sunset = Sun.sunriseBefore(location, t.now());
            return t.includes(sunset);
        });
    }

    public static TimeCondition beforeSunset(Key clockKey, Location location, long amount, TemporalUnit units) {
        return new TimeCondition(clockKey, (t)-> {
            ZonedDateTime sunset = Sun.sunsetAfter(location, t.lastTick());
            sunset = sunset.minus(amount, units);
            return t.includes(sunset);
        });
    }

    public static TimeCondition afterSunset(Key clockKey, Location location, long amount, TemporalUnit units) {
        return new TimeCondition(clockKey, (t)-> {
            ZonedDateTime sunset = Sun.sunsetBefore(location, t.lastTick());
            return t.includes(sunset);
        });
    }
}
