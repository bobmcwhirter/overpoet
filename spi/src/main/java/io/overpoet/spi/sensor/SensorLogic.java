package io.overpoet.spi.sensor;

public interface SensorLogic<T> {
    void start(Sink<T> sink);
}
