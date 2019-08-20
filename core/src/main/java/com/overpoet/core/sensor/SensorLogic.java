package com.overpoet.core.sensor;

public interface SensorLogic<T> {
    void start(Sink<T> sink);
}
