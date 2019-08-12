package com.overpoet.spi;

public interface Manipulator {

    interface Actuator<T> {
        Class<T> datatype();
        void actuate(T value);
    }

    default <T> void register(Actuator<T> actuator) {

    }
}
