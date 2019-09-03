package io.overpoet.spi.sensor;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import io.overpoet.spi.chrono.TimeSlice;
import org.junit.Test;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown;

public class TimeSliceTest {

    @Test
    public void testEdgesWithZone() {
        ZonedDateTime t1 = ZonedDateTime.of(
                2019,
                8,
                14,
                15,
                40,
                00,
                00,
                ZoneId.of("America/New_York"));

        ZonedDateTime t2 = t1.plus(3, MINUTES);

        ZonedDateTime t3 = t1.plus(6, MINUTES);

        ZonedDateTime t4 = t1.plus(9, MINUTES);

        TimeSlice slice = new TimeSlice(t1, t3);

        assertThat(slice.includes(t1)).isFalse();
        assertThat(slice.includes(t2)).isTrue();
        assertThat(slice.includes(t3)).isTrue();
        assertThat(slice.includes(t4)).isFalse();

        try {
            slice = slice.next(t2);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            // expected and correct
        }

        slice = slice.next(t3);

        assertThat(slice.includes(t1)).isFalse();
        assertThat(slice.includes(t2)).isFalse();
        assertThat(slice.includes(t3)).isTrue();
        assertThat(slice.includes(t4)).isFalse();
    }

    @Test
    public void testEdgesWithoutZone() {
        ZonedDateTime t1 = ZonedDateTime.of(
                2019,
                8,
                14,
                15,
                40,
                00,
                00,
                ZoneId.of("America/New_York"));

        ZonedDateTime t2 = t1.plus(3, MINUTES);
        ZonedDateTime t3 = t1.plus(6, MINUTES);
        ZonedDateTime t4 = t1.plus(9, MINUTES);

        LocalDateTime l1 = LocalDateTime.of(
                2019,
                Month.AUGUST,
                14,
                15,
                40,
                00);

        LocalDateTime l2 = l1.plus(3, MINUTES);
        ZonedDateTime l3 = t1.plus(6, MINUTES);
        ZonedDateTime l4 = t1.plus(9, MINUTES);

        TimeSlice slice = new TimeSlice(t1, t3);

        assertThat(slice.includes(t1)).isFalse();
        assertThat(slice.includes(l1)).isFalse();

        assertThat(slice.includes(t2)).isTrue();
        assertThat(slice.includes(l2)).isTrue();

        assertThat(slice.includes(t3)).isTrue();
        assertThat(slice.includes(l3)).isTrue();

        assertThat(slice.includes(t4)).isFalse();
        assertThat(slice.includes(l4)).isFalse();

        try {
            slice = slice.next(t2);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            // expected and correct
        }

        slice = slice.next(t3);

        assertThat(slice.includes(t1)).isFalse();
        assertThat(slice.includes(l1)).isFalse();

        assertThat(slice.includes(t2)).isFalse();
        assertThat(slice.includes(l2)).isFalse();

        assertThat(slice.includes(t3)).isTrue();
        assertThat(slice.includes(l3)).isTrue();

        assertThat(slice.includes(t4)).isFalse();
        assertThat(slice.includes(l4)).isFalse();
    }

}
