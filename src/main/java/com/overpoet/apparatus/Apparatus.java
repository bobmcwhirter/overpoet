package com.overpoet.apparatus;

import java.util.Set;

import com.overpoet.actuator.Actuator;
import com.overpoet.sensor.Sensor;

public interface Apparatus {
    Set<Sensor<?>> sensors();
    Set<Actuator<?>> actuators();
}
