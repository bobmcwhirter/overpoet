package com.overpoet.core.actuator;

import com.overpoet.core.sensor.Sensor;

public interface Switch extends Actuator<Boolean>, Sensor<Boolean> {
    default void actuate(Boolean value) throws ActuationException {
        if ( value ) {
            turnOn();
        } else {
            turnOff();
        }
    }

    void turnOff();
    void turnOn();
}
