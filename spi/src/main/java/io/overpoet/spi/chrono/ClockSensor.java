package io.overpoet.spi.chrono;

import java.time.ZonedDateTime;

import io.overpoet.spi.sensor.TimerDrivenSensor;

import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.SECONDS;

public class ClockSensor extends TimerDrivenSensor<TimeSlice> {

    public ClockSensor(SystemClock clock) {
        super(clock, of(5, SECONDS));
        this.current = new TimeSlice(clock.now(), clock.now());
    }

    @Override
    protected void drive(ZonedDateTime now) {
        this.current = this.current.next(now);
        sink(this.current);
    }

    private TimeSlice current;
}
