package com.overpoet.core.sensor;

import java.time.Duration;
import java.time.ZonedDateTime;

import com.overpoet.core.chrono.TimeSlice;

public class MockClock extends BaseSensorLogic<TimeSlice> {

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

