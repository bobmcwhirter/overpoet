package io.overpoet.spi.sensor;

public interface Sink<T> {
    void sink(T value);
}
