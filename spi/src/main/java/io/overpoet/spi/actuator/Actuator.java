package io.overpoet.spi.actuator;

public interface Actuator<T> {
    void actuate(T value);
}
