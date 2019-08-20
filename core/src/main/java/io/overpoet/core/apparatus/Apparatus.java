package io.overpoet.core.apparatus;

import java.util.Set;

import io.overpoet.Keyed;
import io.overpoet.core.actuator.Actuator;
import io.overpoet.core.sensor.Sensor;

public interface Apparatus extends Keyed {
    Set<Sensor<?>> sensors();
    Set<Actuator<?>> actuators();
}
