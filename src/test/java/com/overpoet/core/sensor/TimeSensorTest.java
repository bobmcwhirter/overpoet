package com.overpoet.core.sensor;

import java.time.LocalDateTime;
import java.time.Month;

import com.overpoet.Key;
import org.junit.Test;

public class TimeSensorTest {

    @Test
    public void testTick() {
        MockClock clock = new MockClock(LocalDateTime.of(
                2019,
                Month.AUGUST,
                14,
                15,
                40,
                00));

        TimeSensor sensor = new TimeSensor(Key.keyOf("system.time"), TimeMetadata.INSTANCE, clock );
    }
}
