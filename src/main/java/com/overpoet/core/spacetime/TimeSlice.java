package com.overpoet.core.spacetime;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeSlice {

    public TimeSlice(LocalDateTime lastTick, LocalDateTime now) {
        this.lastTick = lastTick;
        this.now = now;
    }

    public TimeSlice next(LocalDateTime now) {
        if ( now.isBefore( this.now ) ) {
            throw new IllegalArgumentException( "Time only moves forward");
        }
        return new TimeSlice(this.now, now);
    }

    public LocalDateTime now() {
        return this.now;
    }

    public LocalDateTime lastTick() {
        return this.lastTick;
    }

    public boolean includes(LocalDateTime time) {
        if ( this.now.isEqual(time)) {
            return true;
        }
        if ( time.isBefore(this.now) && time.isAfter(this.lastTick)) {
            return true;
        }

        return false;
    }

    public boolean includes(LocalTime time) {
        LocalTime localTimeNow = this.now.toLocalTime();
        LocalTime localLastTick = this.lastTick.toLocalTime();
        if ( localTimeNow.equals(time)) {
            return true;
        }
        if ( time.isBefore(localTimeNow) && time.isAfter(localLastTick)) {
            return true;
        }

        return false;
    }

    private final LocalDateTime now;
    private final LocalDateTime lastTick;
}
