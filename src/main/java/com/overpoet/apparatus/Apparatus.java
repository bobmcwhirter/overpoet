package com.overpoet.apparatus;

import java.util.Set;

import com.overpoet.Identified;
import com.overpoet.actuator.Actuator;
import com.overpoet.sensor.Sensor;

public interface Apparatus extends Identified {
    Set<Sensor<?>> sensors();
    Set<Actuator<?>> actuators();
}
