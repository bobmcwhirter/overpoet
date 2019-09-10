package io.overpoet.spi.sensor;

import java.time.Duration;
import java.time.ZonedDateTime;

import io.overpoet.spi.chrono.TimeSlice;

public class MockClock extends BaseSensor<TimeSlice> {

    public MockClock(ZonedDateTime currentTime) {
        this.current = new TimeSlice(currentTime, currentTime);
    }

    public void tick(Duration moveAhead) {
        ZonedDateTime now = this.current.now().plus(moveAhead);
        this.current = this.current.next(now);
        sink(this.current);
    }

    public TimeSlice current() {
        return this.current;
    }

    private TimeSlice current;
}

