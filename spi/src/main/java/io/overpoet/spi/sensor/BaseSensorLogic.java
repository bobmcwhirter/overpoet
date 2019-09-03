package io.overpoet.spi.sensor;

public class BaseSensorLogic<T> implements SensorLogic<T>, Sink<T> {

    public void sink(T value) {
        this.sink.sink(value);
    }

    @Override
    public void start(Sink<T> sink) {
        this.sink = sink;
    }

    private Sink<T> sink;
}
