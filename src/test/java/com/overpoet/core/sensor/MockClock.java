package com.overpoet.core.sensor;

import java.time.Duration;
import java.time.LocalDateTime;

public class MockClock extends BaseSensorLogic<TimeSlice> {

    public MockClock(LocalDateTime currentTime) {
        this.current = new TimeSlice(currentTime, currentTime);
    }

    public void tick(Duration moveAhead) {
        LocalDateTime now = this.current.now().plus(moveAhead);
        this.current = this.current.next(now);
        sink(this.current);
    }

    public TimeSlice current() {
        return this.current;
    }

    private TimeSlice current;
}

