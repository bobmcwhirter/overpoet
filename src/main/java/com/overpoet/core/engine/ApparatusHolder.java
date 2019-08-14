package com.overpoet.core.engine;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import com.overpoet.Key;
import com.overpoet.core.actuator.Actuator;
import com.overpoet.core.apparatus.Apparatus;
import com.overpoet.core.engine.state.StateStream;
import com.overpoet.core.sensor.Sensor;
import com.overpoet.core.manipulator.Manipulator;

class ApparatusHolder {

    ApparatusHolder(StateStream state, Apparatus apparatus) {
        this.state = state;
        this.apparatus = apparatus;
        this.key = Key.of(apparatus.id());

        for (Sensor<?> sensor : apparatus.sensors()) {
            this.sensors.add(wrap( sensor));
        }
        for (Actuator<?> actuator : apparatus.actuators()) {
            this.actuators.add(wrap(actuator));
        }
    }

    Manipulator.Apparatus forManipulator(Manipulator manipulator) {
        HashSet<Sensor<?>> wrappedSensors = new HashSet<>();
        for (SensorHolder<?> sensor : this.sensors) {
            try {
                wrappedSensors.add( sensor.forManipulator(manipulator));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }

        HashSet<Manipulator.Actuator<?>> wrappedActuators = new HashSet<>();
        for (ActuatorHolder<?> actuator : this.actuators) {
            wrappedActuators.add( actuator.forManipulator(manipulator));
        }
        return new ManipulatorApparatus(wrappedSensors, wrappedActuators);
    }

    private <T> SensorHolder<T> wrap(Sensor<T> sensor) {
        return new SensorHolder<>(this.state, sensor);
    }

    private <T> ActuatorHolder<T> wrap(Actuator<T> actuator) {
        return new ActuatorHolder<>(this.state, this.key.append( actuator.id()), actuator);
    }

    private final StateStream state;

    private final Apparatus apparatus;
    private final Set<SensorHolder<?>> sensors = new HashSet<>();
    private final Set<ActuatorHolder<?>> actuators = new HashSet<>();
    private final Key key;

    class ManipulatorApparatus implements Manipulator.Apparatus {

        ManipulatorApparatus(Set<Sensor<?>> sensors, Set<Manipulator.Actuator<?>> actuators) {
            this.sensors = sensors;
            this.actuators = actuators;
        }

        @Override
        public String id() {
            return ApparatusHolder.this.apparatus.id();
        }

        @Override
        public Key key() {
            return ApparatusHolder.this.key;
        }

        @Override
        public Set<Sensor<?>> sensors() {
            return this.sensors;
        }

        @Override
        public Set<Manipulator.Actuator<?>> actuators() {
            return this.actuators;
        }

        private final Set<Sensor<?>> sensors;

        private final Set<Manipulator.Actuator<?>> actuators;
    }
}
