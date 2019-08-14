package com.overpoet.core.sensor;

public interface Sink<T> {
    void sink(T value);
}
