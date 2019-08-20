package io.overpoet.core.sensor;

import io.overpoet.Keyed;
import io.overpoet.core.metadata.Metadata;

public interface Sensor<T> extends Keyed {
    Class<T> datatype();
    Metadata<T> metadata();
    void onChange(Sink<T> sink);
}
