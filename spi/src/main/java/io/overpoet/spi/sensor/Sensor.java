package io.overpoet.spi.sensor;

import io.overpoet.spi.Keyed;

public interface Sensor<T> {
    void onChange(Sink<T> sink);
}
