package com.overpoet.core.rule;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalUnit;
import java.util.function.Function;

import com.overpoet.core.sensor.Sensor;
import com.overpoet.core.spacetime.Location;
import com.overpoet.core.spacetime.Sun;
import com.overpoet.core.spacetime.TimeSlice;

public class TimeCondition extends SensorCondition<TimeSlice> {
    public TimeCondition(Sensor<TimeSlice> sensor, Function<TimeSlice, Boolean> condition) {
        super(sensor, condition);
    }

    public static TimeCondition atSunset(Sensor<TimeSlice> clock, Location location) {
        return new TimeCondition(clock, (t)-> {
            ZonedDateTime sunset1 = Sun.sunriseBefore(location.point(), t.now());
            //ZonedDateTime sunset2 = Sun.sunset(location.point(), t.now());

            //return t.includes(sunset1) || t.includes(sunset2);
            return t.includes(sunset1);
        });
    }

    public static TimeCondition beforeSunset(Sensor<TimeSlice> clock, Location location, long amount, TemporalUnit units) {
        return new TimeCondition(clock, (t)-> {
            ZonedDateTime sunset1 = Sun.sunsetAfter(location.point(), t.lastTick());
            sunset1 = sunset1.minus(amount, units);
            //ZonedDateTime sunset2 = Sun.sunset(location.point(), t.now());
            //sunset2 = sunset2.minus(amount, units);

            //return t.includes(sunset1) || t.includes(sunset2);
            return t.includes(sunset1);
        });
    }

    public static TimeCondition afterSunset(Sensor<TimeSlice> clock, Location location, long amount, TemporalUnit units) {
        return new TimeCondition(clock, (t)-> {
            ZonedDateTime sunset1 = Sun.sunsetBefore(location.point(), t.lastTick());
            //sunset1 = sunset1.plus(amount, units);
            //ZonedDateTime sunset2 = Sun.sunset(location.point(), t.now());
            //sunset2 = sunset2.plus(amount, units);

            //return t.includes(sunset1) || t.includes(sunset2);
            return t.includes(sunset1);
        });
    }
}
