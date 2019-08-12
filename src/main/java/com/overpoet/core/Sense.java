package com.overpoet.core;

import com.overpoet.sensor.Sensor;

public interface Sense<T> {
    Sensor<T> sensor();
    T value();
}
