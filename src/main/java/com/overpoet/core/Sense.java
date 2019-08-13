package com.overpoet.core;

import com.overpoet.core.sensor.Sensor;

public interface Sense<T> {
    Sensor<T> sensor();
    T value();
}
