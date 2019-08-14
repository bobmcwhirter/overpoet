package com.overpoet.core.sensor;

import com.overpoet.Keyed;
import com.overpoet.core.metadata.Metadata;

public interface Sensor<T> extends Keyed {
    Class<T> datatype();
    Metadata<T> metadata();
    void onChange(Sink<T> sink);
}
