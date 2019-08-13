package com.overpoet.core;

import com.overpoet.core.actuator.Actuator;

public interface Actuation<T> {

    Actuator<T> actuator();
    T value();
}
