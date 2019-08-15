package com.overpoet.core.spacetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.JulianFields;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class SolarTimesTest {

    @Test
    public void testMidDay() {
        Point here = new Point(new Latitude(36.9485), new Longitude(81.0848));
        LocalDate nowDate = LocalDate.of(
                2019,
                Month.AUGUST,
                15
        );
        LocalTime nowTime = LocalTime.of(15, 5, 00, 00);
        ZonedDateTime now = ZonedDateTime.of(
                nowDate,
                nowTime,
                ZoneId.of("America/New_York"));

        ZonedDateTime sunrise = SolarTimes.sunrise(here, now);

        assertThat(sunrise.getZone()).isEqualTo(ZoneId.of("America/New_York"));
        assertThat(sunrise.getYear()).isEqualTo(2019);
        assertThat(sunrise.getMonth()).isEqualTo(Month.AUGUST);
        assertThat(sunrise.getDayOfMonth()).isEqualTo(15);
        assertThat(sunrise.getHour()).isEqualTo(6);
        assertThat(sunrise.getMinute()).isEqualTo(42);
        assertThat(sunrise.getSecond()).isEqualTo(22);

        ZonedDateTime sunset = SolarTimes.sunset(here, now);

        assertThat(sunset.getZone()).isEqualTo(ZoneId.of("America/New_York"));
        assertThat(sunset.getYear()).isEqualTo(2019);
        assertThat(sunset.getMonth()).isEqualTo(Month.AUGUST);
        assertThat(sunset.getDayOfMonth()).isEqualTo(15);
        assertThat(sunset.getHour()).isEqualTo(20);
        assertThat(sunset.getMinute()).isEqualTo(18);
        assertThat(sunset.getSecond()).isEqualTo(11);
    }

    @Test
    public void testMidnight() {
        Point here = new Point(new Latitude(36.9485), new Longitude(81.0848));
        LocalDate nowDate = LocalDate.of(
                2019,
                Month.AUGUST,
                15
        );
        LocalTime nowTime = LocalTime.of(0, 0, 00, 00);
        ZonedDateTime now = ZonedDateTime.of(
                nowDate,
                nowTime,
                ZoneId.of("America/New_York"));

        ZonedDateTime sunrise = SolarTimes.sunrise(here, now);

        assertThat(sunrise.getZone()).isEqualTo(ZoneId.of("America/New_York"));
        assertThat(sunrise.getYear()).isEqualTo(2019);
        assertThat(sunrise.getMonth()).isEqualTo(Month.AUGUST);
        assertThat(sunrise.getDayOfMonth()).isEqualTo(15);
        assertThat(sunrise.getHour()).isEqualTo(6);
        assertThat(sunrise.getMinute()).isEqualTo(42);
        assertThat(sunrise.getSecond()).isEqualTo(22);

        ZonedDateTime sunset = SolarTimes.sunset(here, now);

        assertThat(sunset.getZone()).isEqualTo(ZoneId.of("America/New_York"));
        assertThat(sunset.getYear()).isEqualTo(2019);
        assertThat(sunset.getMonth()).isEqualTo(Month.AUGUST);
        assertThat(sunset.getDayOfMonth()).isEqualTo(15);
        assertThat(sunset.getHour()).isEqualTo(20);
        assertThat(sunset.getMinute()).isEqualTo(18);
        assertThat(sunset.getSecond()).isEqualTo(11);
    }

    @Test
    public void testImmediatelyPriorToMidnight() {
        Point here = new Point(new Latitude(36.9485), new Longitude(81.0848));
        LocalDate nowDate = LocalDate.of(
                2019,
                Month.AUGUST,
                15
        );
        LocalTime nowTime = LocalTime.of(23, 59, 59, 999);
        ZonedDateTime now = ZonedDateTime.of(
                nowDate,
                nowTime,
                ZoneId.of("America/New_York"));

        ZonedDateTime sunrise = SolarTimes.sunrise(here, now);

        assertThat(sunrise.getZone()).isEqualTo(ZoneId.of("America/New_York"));
        assertThat(sunrise.getYear()).isEqualTo(2019);
        assertThat(sunrise.getMonth()).isEqualTo(Month.AUGUST);
        assertThat(sunrise.getDayOfMonth()).isEqualTo(15);
        assertThat(sunrise.getHour()).isEqualTo(6);
        assertThat(sunrise.getMinute()).isEqualTo(42);
        assertThat(sunrise.getSecond()).isEqualTo(22);

        ZonedDateTime sunset = SolarTimes.sunset(here, now);

        assertThat(sunset.getZone()).isEqualTo(ZoneId.of("America/New_York"));
        assertThat(sunset.getYear()).isEqualTo(2019);
        assertThat(sunset.getMonth()).isEqualTo(Month.AUGUST);
        assertThat(sunset.getDayOfMonth()).isEqualTo(15);
        assertThat(sunset.getHour()).isEqualTo(20);
        assertThat(sunset.getMinute()).isEqualTo(18);
        assertThat(sunset.getSecond()).isEqualTo(11);
    }
}
