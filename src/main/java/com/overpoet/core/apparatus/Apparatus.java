package com.overpoet.core.apparatus;

import java.util.Set;

import com.overpoet.Identified;
import com.overpoet.core.actuator.Actuator;
import com.overpoet.core.sensor.Sensor;

public interface Apparatus extends Identified {
    Set<Sensor<?>> sensors();
    Set<Actuator<?>> actuators();
}
