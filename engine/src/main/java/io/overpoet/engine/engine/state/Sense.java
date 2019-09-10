package io.overpoet.engine.engine.state;

import io.overpoet.spi.Key;

public class Sense<T> {
    public Sense(Key key, T value) {
        this.key = key;
        this.value = value;
    }

    public Key key() {
        return this.key;
    }

    public T value() {
        return this.value;
    }

    @Override
    public String toString() {
        return "[" + key + " = " + this.value + "]";
    }

    private final Key key;
    private final T value;
}
