package com.overpoet.core.engine.state;

import com.overpoet.core.actuator.Actuator;
import com.overpoet.core.Actuation;

class SimpleActuation<T> implements Actuation<T> {

    SimpleActuation(Actuator<T> actuator, T value) {
        this.actuator = actuator;
        this.value = value;
    }

    @Override
    public Actuator<T> actuator() {
        return this.actuator;
    }

    @Override
    public T value() {
        return this.value;
    }

    private final Actuator<T> actuator;
    private final T value;
}
