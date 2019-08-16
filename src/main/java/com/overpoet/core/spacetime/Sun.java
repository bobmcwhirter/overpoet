package com.overpoet.core.spacetime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.JulianFields;

import static java.lang.Math.round;

public class Sun {

    public static ZonedDateTime sunriseBefore(Point point, ZonedDateTime date) {
        return SunTimes.sunriseBefore(point, date);
    }

    public static ZonedDateTime sunriseAfter(Point point, ZonedDateTime date) {
        return SunTimes.sunriseAfter(point, date);
    }


    public static ZonedDateTime sunsetBefore(Point point, ZonedDateTime date) {
        return SunTimes.sunsetBefore(point, date);
    }

    public static ZonedDateTime sunsetAfter(Point point, ZonedDateTime date) {
        return SunTimes.sunsetAfter(point, date);
    }

    public static ZonedDateTime solarNoon(Point point, ZonedDateTime date) {
        return SunTimes.solarNoon(point, date);
    }

    static ZonedDateTime julianToZoned(double julian, ZoneId zone) {
        LocalDate date = LocalDate.MIN.with(JulianFields.JULIAN_DAY, (long) julian);

        double remainder = (julian - ((long) julian));

        long totalSeconds = (long) (remainder * 86400) + (12 * 60 * 60);

        int days, hours, minutes, seconds = 0;

        days = (int) (totalSeconds / ( 60 * 60 * 24));
        totalSeconds = totalSeconds - (days * 60 * 60 * 24);

        date = date.plus(days, ChronoUnit.DAYS);

        hours = (int) (totalSeconds / (60 * 60));
        totalSeconds = totalSeconds - (hours * 60 * 60);

        minutes = (int) (totalSeconds / 60);
        totalSeconds = totalSeconds - (minutes * 60);

        seconds = (int) totalSeconds;

        LocalTime time = LocalTime.of(hours, minutes, seconds);

        return OffsetDateTime.of(date, time, ZoneOffset.UTC).atZoneSameInstant(zone);
    }

    static double radians(double degrees) {
        return degrees * Math.PI / 180;
    }

    static double degrees(double radians) {
        return radians * 180 / Math.PI;
    }

    static long julianCycle(ZonedDateTime date, Longitude longitude) {
        //return round((date.getLong(JulianFields.JULIAN_DAY) - 2451545.0 - 0.0009) - (longitude.decimalDegrees() / 360));
        return round( (Julian.toJulian2000(date) - 0.009) - (longitude.decimalDegrees() / 360));
    }

    static double jStarStar(double hourAngle, Longitude longitude, long julianCycle) {
        return 2451545 + 0.0009 + ((hourAngle + longitude.decimalDegrees()) / 360) + julianCycle;
    }

}
