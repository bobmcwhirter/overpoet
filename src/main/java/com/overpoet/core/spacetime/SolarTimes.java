package com.overpoet.core.spacetime;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.JulianFields;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.round;
import static java.lang.Math.sin;

public class SolarTimes {

    public static ZonedDateTime sunrise(Point point, ZonedDateTime date) {
        return sunriseSunset(point, date)[0];
    }

    public static ZonedDateTime sunset(Point point, ZonedDateTime date) {
        return sunriseSunset(point, date)[1];
    }

    public static ZonedDateTime[] sunriseSunset(Point point, ZonedDateTime date) {
        long julianCycle = julianCycle(date, point.longitude());
        double meanSolarNoon = meanSolarNoon(point.longitude(), julianCycle);

        double solarMeanAnomaly = solarMeanAnomaly(meanSolarNoon);
        double equationOfTheCenter = equationOfTheCenter(solarMeanAnomaly);

        double eclipticLongitude = eclipticLongitude(solarMeanAnomaly, equationOfTheCenter);

        double declinationOfTheSun = declinationOfTheSun(eclipticLongitude);

        double hourAngle = hourAngle(point.latitude(), declinationOfTheSun);

        double jStarStar = jStarStar(hourAngle, point.longitude(), julianCycle);

        double set = jStarStar + (0.0053 * sin(radians(solarMeanAnomaly))) - (0.0069 * sin(radians(2 * eclipticLongitude)));

        double solarTransit = solarTransit(meanSolarNoon, solarMeanAnomaly, eclipticLongitude);
        double rise = solarTransit - ( set - solarTransit );

        return new ZonedDateTime[] {
                julianToZoned(rise, date.getZone()),
                julianToZoned(set, date.getZone()),
        };
    }


    public static ZonedDateTime solarNoon(Point point, ZonedDateTime date) {
        long julianCycle = julianCycle(date, point.longitude());
        double meanSolarNoon = meanSolarNoon(point.longitude(), julianCycle);

        double solarMeanAnomaly = solarMeanAnomaly(meanSolarNoon);
        double equationOfTheCenter = equationOfTheCenter(solarMeanAnomaly);
        double eclipticLongitude = eclipticLongitude(solarMeanAnomaly, equationOfTheCenter);

        double solarTransit = solarTransit(meanSolarNoon, solarMeanAnomaly, eclipticLongitude);

        return julianToZoned(solarTransit, date.getZone());
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
        return round((date.getLong(JulianFields.JULIAN_DAY) - 2451545.0 - 0.0009) - (longitude.decimalDegrees() / 360));
    }

    static double meanSolarNoon(Longitude longitude, double julianCycle) {
        return 2451545 + 0.0009 + (longitude.decimalDegrees() / 360) + julianCycle;
    }

    static double solarMeanAnomaly(double meanSolarNoon) {
        return (357.5291 + (0.98560028 * (meanSolarNoon - 2451545))) % 360;
    }

    static double equationOfTheCenter(double solarMeanAnomoaly) {
        return (1.9148 * sin(radians(solarMeanAnomoaly))) + (0.0200 * sin(radians(2 * solarMeanAnomoaly))) + (0.0003 * sin(radians(3 * solarMeanAnomoaly)));
    }

    static double eclipticLongitude(double solarMeanAnomaly, double equationOfTheCenter) {
        return (solarMeanAnomaly + equationOfTheCenter + 180 + 102.9372) % 360;
    }

    static double solarTransit(double meanSolarNoon, double solarMeanAnomaly, double eclipticLongitude) {
        return meanSolarNoon + (0.0053 * sin(radians(solarMeanAnomaly))) - (0.0069 * sin(2 * radians(eclipticLongitude)));
    }

    static double declinationOfTheSun(double eclipticLongitude) {
        return degrees(Math.asin(sin(radians(eclipticLongitude)) * sin(radians(23.45))));
    }

    static double hourAngle(Latitude latitude, double declinationOfTheSun) {
        double numerator = sin(radians(-0.83)) - (sin(radians(latitude.decimalDegrees())) * sin(radians(declinationOfTheSun)));
        double denominator = cos(radians(latitude.decimalDegrees())) * cos(radians(declinationOfTheSun));
        double result = degrees(acos(numerator / denominator));

        return result;
    }

    static double jStarStar(double hourAngle, Longitude longitude, long julianCycle) {
        return 2451545 + 0.0009 + ((hourAngle + longitude.decimalDegrees()) / 360) + julianCycle;
    }

}
