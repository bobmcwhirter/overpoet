package com.overpoet.apparatus;

import java.util.HashSet;
import java.util.Set;

import com.overpoet.actuator.Actuator;
import com.overpoet.sensor.Sensor;

public class SimpleApparatus implements Apparatus {
    public SimpleApparatus(String id, Set<Sensor<?>> sensors, Set<Actuator<?>> actuators) {
        this.id = id;
        if ( sensors != null ) {
            this.sensors.addAll(sensors);
        }
        if ( actuators != null ) {
            this.actuators.addAll(actuators);
        }
    }

    public String id() {
        return this.id;
    }

    @Override
    public Set<Sensor<?>> sensors() {
        return this.sensors;
    }

    @Override
    public Set<Actuator<?>> actuators() {
        return this.actuators;
    }


    private final String id;
    final private Set<Sensor<?>> sensors = new HashSet<>();
    final private Set<Actuator<?>> actuators = new HashSet<>();
}
