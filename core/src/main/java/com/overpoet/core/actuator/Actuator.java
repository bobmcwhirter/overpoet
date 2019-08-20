package com.overpoet.core.actuator;

import com.overpoet.Keyed;

public interface Actuator<T> extends Keyed {
    Class<T> datatype();
    void actuate(T value) throws ActuationException;
}
