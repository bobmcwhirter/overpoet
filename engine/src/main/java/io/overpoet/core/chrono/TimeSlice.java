package io.overpoet.core.chrono;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public class TimeSlice {

    public TimeSlice(ZonedDateTime lastTick, ZonedDateTime now) {
        this.lastTick = lastTick;
        this.now = now;
    }

    public TimeSlice next(ZonedDateTime now) {
        if ( now.isBefore( this.now ) ) {
            throw new IllegalArgumentException( "Time only moves forward");
        }
        return new TimeSlice(this.now, now);
    }

    public ZonedDateTime now() {
        return this.now;
    }

    public ZonedDateTime lastTick() {
        return this.lastTick;
    }

    public boolean includes(ZonedDateTime time) {
        if ( this.now.isEqual(time)) {
            return true;
        }
        if ( time.isBefore(this.now) && time.isAfter(this.lastTick)) {
            return true;
        }

        return false;
    }

    public boolean includes(LocalDateTime time) {
        if ( this.now.toLocalDateTime().isEqual(time)) {
            return true;
        }
        if ( time.isBefore(this.now.toLocalDateTime()) && time.isAfter(this.lastTick.toLocalDateTime())) {
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

    public String toString() {
        return "[TimeSlice: " + this.lastTick + " >> " + this.now + "]";
    }

    private final ZonedDateTime now;
    private final ZonedDateTime lastTick;
}
