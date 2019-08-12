package com.overpoet.actuator;

import com.overpoet.Identified;

public interface Actuator<T> extends Identified {
    Class<T> datatype();
    void actuate(T value) throws ActuationException;
}
