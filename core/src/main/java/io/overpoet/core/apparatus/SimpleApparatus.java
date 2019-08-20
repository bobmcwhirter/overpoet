package io.overpoet.core.apparatus;

import java.util.HashSet;
import java.util.Set;

import io.overpoet.Key;
import io.overpoet.core.actuator.Actuator;
import io.overpoet.core.sensor.Sensor;

public class SimpleApparatus implements Apparatus {
    public SimpleApparatus(Key key, Set<Sensor<?>> sensors, Set<Actuator<?>> actuators) {
        this.key = key;
        if ( sensors != null ) {
            this.sensors.addAll(sensors);
        }
        if ( actuators != null ) {
            this.actuators.addAll(actuators);
        }
    }

    public Key key() {
        return this.key;
    }

    @Override
    public Set<Sensor<?>> sensors() {
        return this.sensors;
    }

    @Override
    public Set<Actuator<?>> actuators() {
        return this.actuators;
    }


    private final Key key;
    final private Set<Sensor<?>> sensors = new HashSet<>();
    final private Set<Actuator<?>> actuators = new HashSet<>();
}
