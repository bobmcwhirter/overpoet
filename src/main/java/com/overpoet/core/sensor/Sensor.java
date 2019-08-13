package com.overpoet.core.sensor;

import com.overpoet.Identified;

public interface Sensor<T> extends Identified {

    interface Sink<T> {
        void sink(T value) throws SenseException;
    }

    Class<T> datatype();
    void initialize(Sink<T> sink);

    default T current() {
        return null;
    }

}
