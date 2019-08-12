package com.overpoet.core.state;

import com.overpoet.core.Sense;
import com.overpoet.sensor.Sensor;

class SimpleSense<T> implements Sense<T> {

    SimpleSense(Sensor<T> sensor, T value) {
        this.sensor = sensor;
        this.value = value;
    }

    @Override
    public Sensor<T> sensor() {
        return this.sensor;
    }

    @Override
    public T value() {
        return this.value;
    }

    private final Sensor<T> sensor;
    private final T value;
}
