package io.overpoet.lutron;

import io.overpoet.spi.sensor.SensorLogic;
import io.overpoet.spi.sensor.Sink;

public class DelegateSensorLogic<T> implements SensorLogic<T> {
    @Override
    public void start(Sink<T> sink) {
        this.sink = sink;
    }

    public void delegate(T value) {
        this.sink.sink(value);
    }

    private Sink<T> sink;
}
