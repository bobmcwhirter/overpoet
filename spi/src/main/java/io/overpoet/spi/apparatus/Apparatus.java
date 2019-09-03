package io.overpoet.spi.apparatus;

import java.util.Set;

import io.overpoet.spi.Keyed;
import io.overpoet.spi.actuator.Actuator;
import io.overpoet.spi.sensor.Sensor;

public interface Apparatus extends Keyed {
    ApparatusType type();
    Set<Sensor<?>> sensors();
    Set<Actuator<?>> actuators();
}
