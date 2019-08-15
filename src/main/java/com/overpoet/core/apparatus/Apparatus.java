package com.overpoet.core.apparatus;

import java.util.Set;

import com.overpoet.Keyed;
import com.overpoet.core.actuator.Actuator;
import com.overpoet.core.sensor.Sensor;

public interface Apparatus extends Keyed {
    Set<Sensor<?>> sensors();
    Set<Actuator<?>> actuators();
}
