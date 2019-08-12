package com.overpoet.sensor;

public interface Sensor<T> {

    interface Sink<T> {
        void sink(T value);
    }

    Class<T> datatype();

    void initialize(Sink<T> sink);

    default T current() {
        return null;
    }

}
