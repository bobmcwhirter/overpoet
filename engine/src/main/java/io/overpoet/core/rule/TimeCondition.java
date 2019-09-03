package io.overpoet.core.rule;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalUnit;
import java.util.function.Function;

import io.overpoet.spi.sensor.Sensor;
import io.overpoet.spi.geo.Location;
import io.overpoet.core.solar.Sun;
import io.overpoet.spi.chrono.TimeSlice;

public class TimeCondition extends SensorCondition<TimeSlice> {
    public TimeCondition(Sensor<TimeSlice> sensor, Function<TimeSlice, Boolean> condition) {
        super(sensor, condition);
    }

    public static TimeCondition atSunrise(Sensor<TimeSlice> clock, Location location) {
        return new TimeCondition(clock, (t)-> {
            ZonedDateTime sunrise = Sun.sunriseBefore(location, t.now());
            return t.includes(sunrise);
        });
    }

    public static TimeCondition beforeSunrise(Sensor<TimeSlice> clock, Location location, long amount, TemporalUnit units) {
        return new TimeCondition(clock, (t)-> {
            ZonedDateTime sunrise = Sun.sunriseAfter(location, t.lastTick());
            sunrise = sunrise.minus(amount, units);
            return t.includes(sunrise);
        });
    }

    public static TimeCondition afterSunrise(Sensor<TimeSlice> clock, Location location, long amount, TemporalUnit units) {
        return new TimeCondition(clock, (t)-> {
            ZonedDateTime sunrise = Sun.sunriseBefore(location, t.lastTick());
            return t.includes(sunrise);
        });
    }

    public static TimeCondition atSunset(Sensor<TimeSlice> clock, Location location) {
        return new TimeCondition(clock, (t)-> {
            ZonedDateTime sunset = Sun.sunriseBefore(location, t.now());
            return t.includes(sunset);
        });
    }

    public static TimeCondition beforeSunset(Sensor<TimeSlice> clock, Location location, long amount, TemporalUnit units) {
        return new TimeCondition(clock, (t)-> {
            ZonedDateTime sunset = Sun.sunsetAfter(location, t.lastTick());
            sunset = sunset.minus(amount, units);
            return t.includes(sunset);
        });
    }

    public static TimeCondition afterSunset(Sensor<TimeSlice> clock, Location location, long amount, TemporalUnit units) {
        return new TimeCondition(clock, (t)-> {
            ZonedDateTime sunset = Sun.sunsetBefore(location, t.lastTick());
            return t.includes(sunset);
        });
    }
}
