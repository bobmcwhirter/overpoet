package com.overpoet.core.engine;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import com.overpoet.core.actuator.Actuator;
import com.overpoet.core.apparatus.Apparatus;
import com.overpoet.core.apparatus.SimpleApparatus;
import com.overpoet.core.engine.state.InMemoryStateStream;
import com.overpoet.core.sensor.Sensor;
import com.overpoet.core.manipulator.Manipulator;

class ApparatusHolder {

    ApparatusHolder(InMemoryStateStream state, Apparatus apparatus) {
        this.state = state;
        this.apparatus = apparatus;

        for (Sensor<?> sensor : apparatus.sensors()) {
            System.err.println( "register sensor: " + sensor.key());
            this.sensors.add(wrap( sensor));
        }
        for (Actuator<?> actuator : apparatus.actuators()) {
            this.actuators.add(wrap(actuator));
        }
    }

    Apparatus forManipulator(Manipulator manipulator) {
        HashSet<Sensor<?>> wrappedSensors = new HashSet<>();
        for (SensorHolder<?> sensor : this.sensors) {
            try {
                wrappedSensors.add( sensor.forManipulator(manipulator));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }

        HashSet<Actuator<?>> wrappedActuators = new HashSet<>();
        for (ActuatorHolder<?> actuator : this.actuators) {
            wrappedActuators.add( actuator.forManipulator(manipulator));
        }
        return new SimpleApparatus(apparatus.key(), wrappedSensors, wrappedActuators);
    }

    private <T> SensorHolder<T> wrap(Sensor<T> sensor) {
        return new SensorHolder<>(this.state, sensor);
    }

    private <T> ActuatorHolder<T> wrap(Actuator<T> actuator) {
        return new ActuatorHolder<>(this.state, actuator);
    }

    private final InMemoryStateStream state;

    private final Apparatus apparatus;
    private final Set<SensorHolder<?>> sensors = new HashSet<>();
    private final Set<ActuatorHolder<?>> actuators = new HashSet<>();

}
