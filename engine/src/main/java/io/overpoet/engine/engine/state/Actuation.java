package io.overpoet.engine.engine.state;

import io.overpoet.spi.actuator.Actuator;

public class Actuation<T> {

    public Actuation(Actuator<T> actuator, T value) {
        this.actuator = actuator;
        this.value = value;
    }

    public Actuator<T> actuator() {
        return this.actuator;
    }

    public T value() {
        return this.value;
    }

    private final Actuator<T> actuator;
    private final T value;

}
