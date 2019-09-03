package io.overpoet.core.engine.state;

import io.overpoet.core.sensor.Sensor;

public class Sense<T> {
    public Sense(Sensor<T> sensor, T value) {
        this.sensor = sensor;
        this.value = value;
    }

    public Sensor<T> sensor() {
        return this.sensor;
    }

    public T value() {
        return this.value;
    }

    @Override
    public String toString() {
        return "[" + this.sensor.key() + " = " + this.value + "]";
    }

    private final Sensor<T> sensor;
    private final T value;
}
