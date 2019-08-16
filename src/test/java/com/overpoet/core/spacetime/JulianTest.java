package com.overpoet.core.spacetime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

public class JulianTest {

    @Test
    public void testConversionUTC() {
        double j = Julian.toJulian(ZonedDateTime.of(LocalDate.of(2019,
                                                      Month.AUGUST,
                                                      16),
                                         LocalTime.of(14, 47, 00),
                                         ZoneOffset.UTC
        ));

        assertThat(j).isEqualTo(2458712.115972, offset(0.001));
    }

    @Test
    public void testConversionTZ() {
        double j = Julian.toJulian(ZonedDateTime.of(LocalDate.of(2019,
                                                                 Month.AUGUST,
                                                                 16),
                                                    LocalTime.of(10, 47, 00),
                                                    ZoneId.of("America/New_York")
        ));

        assertThat(j).isEqualTo(2458712.115972, offset(0.001));
    }
}
