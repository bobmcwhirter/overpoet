package com.overpoet.core;

import com.overpoet.actuator.Actuator;

public interface Actuation<T> {

    Actuator<T> actuator();
    T value();
}
