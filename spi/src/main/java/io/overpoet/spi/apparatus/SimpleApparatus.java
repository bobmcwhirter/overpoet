package io.overpoet.spi.apparatus;

import java.util.HashSet;
import java.util.Set;

import io.overpoet.spi.Key;
import io.overpoet.spi.actuator.Actuator;
import io.overpoet.spi.metadata.ApparatusMetadata;
import io.overpoet.spi.sensor.Sensor;

public class SimpleApparatus implements Apparatus {
    public SimpleApparatus(ApparatusMetadata metadata, Key key, Set<Sensor<?>> sensors, Set<Actuator<?>> actuators) {
        this.metadata = metadata;
        this.key = key;
        if ( sensors != null ) {
            this.sensors.addAll(sensors);
        }
        if ( actuators != null ) {
            this.actuators.addAll(actuators);
        }
    }

    /*
    @Override
    public ApparatusType type() {
        return this.type;
    }

     */

    @Override
    public ApparatusMetadata metadata() {
        return this.metadata;
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

    private final ApparatusMetadata metadata;
    private final Key key;
    final private Set<Sensor<?>> sensors = new HashSet<>();
    final private Set<Actuator<?>> actuators = new HashSet<>();
}
