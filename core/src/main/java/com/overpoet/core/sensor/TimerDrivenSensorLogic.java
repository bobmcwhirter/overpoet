package com.overpoet.core.sensor;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Timer;
import java.util.TimerTask;

import com.overpoet.core.chrono.SystemClock;

public abstract class TimerDrivenSensorLogic<T> extends BaseSensorLogic<T> {

    protected TimerDrivenSensorLogic(SystemClock clock, Duration period) {
        this.clock = clock;
        this.period = period;
        this.timer = new Timer(true);
    }

    @Override
    public void start(Sink<T> sink) {
        super.start(sink);
        this.timer.scheduleAtFixedRate(new Task(), 0, this.period.toMillis());
    }

    protected abstract void drive(ZonedDateTime now);

    private final Timer timer;
    private final SystemClock clock;
    private final Duration period;

    private class Task extends TimerTask {
        @Override
        public void run() {
            drive(clock.now());
        }
    }
}
