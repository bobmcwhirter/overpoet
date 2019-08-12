package com.overpoet.actuator;

public interface Actuator<T> {
    Class<T> datatype();
    void actuate(T value);
}
