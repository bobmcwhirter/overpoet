package io.overpoet.core.actuator;

import io.overpoet.Keyed;

public interface Actuator<T> extends Keyed {
    Class<T> datatype();
    void actuate(T value) throws ActuationException;
}
