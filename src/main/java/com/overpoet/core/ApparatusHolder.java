package com.overpoet.core;

import java.util.HashSet;
import java.util.Set;

import com.overpoet.actuator.Actuator;
import com.overpoet.apparatus.Apparatus;
import com.overpoet.core.state.StateStream;
import com.overpoet.sensor.Sensor;
import com.overpoet.manipulator.Manipulator;

class ApparatusHolder {

    ApparatusHolder(StateStream state, Apparatus apparatus) {
        this.state = state;
        this.apparatus = apparatus;
        this.key = new KeyImpl(apparatus.id());

        for (Sensor<?> sensor : apparatus.sensors()) {
            this.sensors.add(wrap( sensor));
        }
        for (Actuator<?> actuator : apparatus.actuators()) {
            this.actuators.add(wrap(actuator));
        }
    }

    Manipulator.Apparatus forManipulator(Manipulator manipulator) {
        HashSet<Manipulator.Sensor<?>> wrappedSensors = new HashSet<>();
        for (SensorHolder<?> sensor : this.sensors) {
            wrappedSensors.add( sensor.forManipulator(manipulator));
        }

        HashSet<Manipulator.Actuator<?>> wrappedActuators = new HashSet<>();
        for (ActuatorHolder<?> actuator : this.actuators) {
            wrappedActuators.add( actuator.forManipulator(manipulator));
        }
        return new ManipulatorApparatus(wrappedSensors, wrappedActuators);
    }

    private <T> SensorHolder<T> wrap(Sensor<T> sensor) {
        return new SensorHolder<>(this.state, this.key.append( sensor.id()), sensor);
    }

    private <T> ActuatorHolder<T> wrap(Actuator<T> actuator) {
        return new ActuatorHolder<>(this.state, this.key.append( actuator.id()), actuator);
    }

    private final StateStream state;

    private final Apparatus apparatus;
    private final Set<SensorHolder<?>> sensors = new HashSet<>();
    private final Set<ActuatorHolder<?>> actuators = new HashSet<>();
    private final KeyImpl key;

    class ManipulatorApparatus implements Manipulator.Apparatus {

        ManipulatorApparatus(Set<Manipulator.Sensor<?>> sensors, Set<Manipulator.Actuator<?>> actuators) {
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
        public Set<Manipulator.Sensor<?>> sensors() {
            return this.sensors;
        }

        @Override
        public Set<Manipulator.Actuator<?>> actuators() {
            return this.actuators;
        }

        private final Set<Manipulator.Sensor<?>> sensors;

        private final Set<Manipulator.Actuator<?>> actuators;
    }
}
