package io.overpoet.spi.sensor;

import io.overpoet.spi.Keyed;
import io.overpoet.spi.metadata.Metadata;

public interface Sensor<T> extends Keyed {
    Class<T> datatype();
    Metadata<T> metadata();
    void onChange(Sink<T> sink);
}
