package io.overpoet.engine.engine.state;

import io.overpoet.spi.Key;
import io.overpoet.spi.actuator.Actuator;

public class Actuation<T> {

    public Actuation(Key key, T value) {
        this.key = key;
        this.value = value;
    }

    public Key key() {
        return this.key;
    }

    public T value() {
        return this.value;
    }

    private final Key key;
    private final T value;

}
