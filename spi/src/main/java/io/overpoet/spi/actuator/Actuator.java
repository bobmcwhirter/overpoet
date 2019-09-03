package io.overpoet.spi.actuator;

import io.overpoet.spi.Keyed;

public interface Actuator<T> extends Keyed {
    Class<T> datatype();
    void actuate(T value) throws ActuationException;
}
