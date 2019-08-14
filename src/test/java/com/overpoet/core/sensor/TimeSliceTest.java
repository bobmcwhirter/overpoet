package com.overpoet.core.sensor;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown;

public class TimeSliceTest {

    @Test
    public void testEdges() {
        LocalDateTime t1 = LocalDateTime.of(
                2019,
                Month.AUGUST,
                14,
                15,
                40,
                00);

        LocalDateTime t2 = t1.plus(3, ChronoUnit.MINUTES);

        LocalDateTime t3 = t1.plus(6, ChronoUnit.MINUTES);

        LocalDateTime t4 = t1.plus(9, ChronoUnit.MINUTES);

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

}
