package io.overpoet.core.solar;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import io.overpoet.core.chrono.DeltaT;
import io.overpoet.core.geo.AzimuthZenithAngle;
import io.overpoet.core.measurement.Distance;
import io.overpoet.core.geo.Location;
import io.overpoet.core.geo.Point;
import org.fest.assertions.data.Offset;
import org.junit.Test;

import static io.overpoet.core.geo.Latitude.north;
import static io.overpoet.core.measurement.Distance.feet;
import static io.overpoet.core.geo.Longitude.west;
import static java.time.ZoneId.of;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.fest.assertions.api.Assertions.assertThat;

public class SunTest {

    public static Location HERE = new Location() {
        @Override
        public Point point() {
            return new Point(north(36.57), west(81.05));
        }

        @Override
        public ZoneId getZoneId() {
            return of("America/New_York");
        }

        @Override
        public Distance elevation() {
            return feet(3000);
        }
    };

    public static LocalDate TODAY = LocalDate.of(
            2019,
            Month.AUGUST,
            15
    );

    public static LocalDate YESTERDAY = TODAY.minus(1, DAYS);

    public static LocalDate TOMORROW = TODAY.plus(1, DAYS);

    public static ZonedDateTime YESTERDAY_SUNRISE = at(YESTERDAY, 6, 41);

    public static ZonedDateTime YESTERDAY_SUNSET = at(YESTERDAY, 20, 17);

    public static ZonedDateTime TODAY_SUNRISE = at(TODAY, 6, 42);

    public static ZonedDateTime TODAY_SUNSET = at(TODAY, 20, 16);

    public static ZonedDateTime TOMORROW_SUNRISE = at(TOMORROW, 6, 43);

    public static ZonedDateTime TOMORROW_SUNSET = at(TOMORROW, 20, 14);

    public static ZonedDateTime at(LocalDate date, int hour, int minute) {
        return ZonedDateTime.of(date, LocalTime.of(hour, minute), of("America/New_York"));
    }

    @Test
    public void testMidDay_sunriseBefore() {
        assertClose(YESTERDAY_SUNRISE, Sun.sunriseBefore(HERE, at(YESTERDAY, 14, 0)));
        assertClose(TODAY_SUNRISE, Sun.sunriseBefore(HERE, at(TODAY, 12, 0)));
        assertClose(TOMORROW_SUNRISE, Sun.sunriseBefore(HERE, at(TOMORROW, 12, 0)));
    }

    @Test
    public void testMidnight_sunriseBefore() {
        assertNotClose(YESTERDAY_SUNRISE, Sun.sunriseBefore(HERE, at(YESTERDAY, 0, 0)));
        assertClose(YESTERDAY_SUNRISE, Sun.sunriseBefore(HERE, at(TODAY, 0, 0)));
        assertClose(TODAY_SUNRISE, Sun.sunriseBefore(HERE, at(TOMORROW, 0, 0)));
    }

    @Test
    public void testImmediatelyBeforeMidnight_sunriseBefore() {
        assertClose(YESTERDAY_SUNRISE, Sun.sunriseBefore(HERE, at(YESTERDAY, 23, 59)));
        assertClose(TODAY_SUNRISE, Sun.sunriseBefore(HERE, at(TODAY, 23, 59)));
        assertClose(TOMORROW_SUNRISE, Sun.sunriseBefore(HERE, at(TOMORROW, 23, 59)));
    }

    @Test
    public void testShortlyBeforeSunrise_sunriseBefore() {
        assertNotClose(YESTERDAY_SUNRISE, Sun.sunriseBefore(HERE, YESTERDAY_SUNRISE.minus(5, MINUTES)));
        assertClose(YESTERDAY_SUNRISE, Sun.sunriseBefore(HERE, TODAY_SUNRISE.minus(5, MINUTES)));
        assertClose(TODAY_SUNRISE, Sun.sunriseBefore(HERE, TOMORROW_SUNRISE.minus(5, MINUTES)));
    }

    @Test
    public void testImmediatelyBeforeSunrise_sunriseBefore() {
        assertNotClose(YESTERDAY_SUNRISE, Sun.sunriseBefore(HERE, YESTERDAY_SUNRISE.minus(1, MINUTES)));
        assertClose(YESTERDAY_SUNRISE, Sun.sunriseBefore(HERE, TODAY_SUNRISE.minus(1, MINUTES)));
        assertClose(TODAY_SUNRISE, Sun.sunriseBefore(HERE, TOMORROW_SUNRISE.minus(1, MINUTES)));
    }

    protected void assertClose(ZonedDateTime left, ZonedDateTime right) {
        Duration duration = Duration.between(left, right);
        assertThat(Math.abs(duration.toMinutes())).isLessThan(2);
    }

    protected void assertNotClose(ZonedDateTime left, ZonedDateTime right) {
        Duration duration = Duration.between(left, right);
        assertThat(Math.abs(duration.toMinutes())).isGreaterThan(60 * 20);
    }

    @Test
    public void testPosition() {
        ZonedDateTime t = TODAY_SUNRISE;
        AzimuthZenithAngle pos = SPA.calculateSolarPosition(t, 36.57, -81.05, 1000, DeltaT.estimate(t));

        assertThat(pos.getZenithAngle()).isEqualTo(90.0, Offset.offset(1.0));
    }

}
