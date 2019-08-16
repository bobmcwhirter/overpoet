package com.overpoet.core.spacetime;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class Julian {

    private static int EPOCH_YEAR = 4712;

    public static double toJulian(ZonedDateTime time) {
        ZonedDateTime utc = time.withZoneSameInstant(ZoneOffset.UTC);

        int totalYears = utc.getYear() + EPOCH_YEAR;
        int leapYears = leapYearsSinceEpoch(totalYears);
        int nonLeapYears = totalYears - leapYears;

        int totalDays = (leapYears * 366) + (nonLeapYears * 365) - 10;

        long daysSinceStartOfYear = Duration.between(utc.withMonth(1).withDayOfMonth(1), utc).toDays();
        totalDays += daysSinceStartOfYear;

        long secondsSinceNoon = 0;
        ZonedDateTime noonToday = utc.withHour(12).withMinute(0).withSecond(0);
        if (time.isAfter(noonToday)) {
            secondsSinceNoon = Duration.between(noonToday, time).toSeconds();
        } else {
            secondsSinceNoon = Duration.between(noonToday.minus(1, ChronoUnit.DAYS), time).toSeconds() + 43200;
        }

        double fractionalDays = (double) secondsSinceNoon / 86400.0;

        return totalDays + fractionalDays;
    }

    public static double toJulian2000(ZonedDateTime time) {
        return toJulian(time) - 2451545.0;
    }

    private static int leapYearsSinceEpoch(int numYears) {
        int leapYears = 0;
        for (int i = 0; i < numYears; ++i) {
            int year = -EPOCH_YEAR + i;
            if (year < 1582) {
                if (year % 4 == 0) {
                    ++leapYears;
                }
            } else if (year % 4 == 0) {
                if (year % 400 == 0) {
                    ++leapYears;
                } else if (year % 100 == 0) {
                    // not a leap year
                } else {
                    ++leapYears;
                }
            }
        }
        return leapYears;
    }
}
