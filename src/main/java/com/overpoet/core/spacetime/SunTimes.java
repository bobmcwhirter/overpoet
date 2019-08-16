package com.overpoet.core.spacetime;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static com.overpoet.core.spacetime.Sun.degrees;
import static com.overpoet.core.spacetime.Sun.julianCycle;
import static com.overpoet.core.spacetime.Sun.julianToZoned;
import static com.overpoet.core.spacetime.Sun.radians;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.time.temporal.ChronoUnit.DAYS;

public class SunTimes {

    /*
    public static ZonedDateTime sunrise(Point point, ZonedDateTime date) {
        return sunriseSunset(point, date)[0];
    }

    public static ZonedDateTime sunset(Point point, ZonedDateTime date) {
        return sunriseSunset(point, date)[1];
    }
     */

    public static ZonedDateTime sunriseBefore(Point point, ZonedDateTime date) {
        ZonedDateTime[] pair = sunriseSunset(point, date);
        ZonedDateTime curDate = date;
        while ( pair[0].isAfter(date)) {
            curDate = curDate.minus(1, DAYS);
            pair = sunriseSunset(point, curDate);
        }
        return pair[0];
    }

    public static ZonedDateTime sunriseAfter(Point point, ZonedDateTime date) {
        ZonedDateTime[] pair = sunriseSunset(point, date);
        ZonedDateTime curDate = date;
        while ( pair[0].isBefore(date)) {
            curDate = curDate.plus(1, DAYS);
            pair = sunriseSunset(point, curDate);
        }
        return pair[0];
    }

    public static ZonedDateTime sunsetBefore(Point point, ZonedDateTime date) {
        ZonedDateTime[] pair = sunriseSunset(point, date);
        ZonedDateTime curDate = date;
        while ( pair[1].isAfter(date)) {
            curDate = curDate.minus(1, DAYS);
            pair = sunriseSunset(point, curDate);
        }
        return pair[1];
    }

    public static ZonedDateTime sunsetAfter(Point point, ZonedDateTime date) {
        ZonedDateTime[] pair = sunriseSunset(point, date);
        ZonedDateTime curDate = date;
        while ( pair[1].isBefore(date)) {
            curDate = curDate.plus(1, DAYS);
            pair = sunriseSunset(point, curDate);
        }
        return pair[1];
    }

    private static ZonedDateTime[] sunriseSunset(Point point, ZonedDateTime date) {
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
