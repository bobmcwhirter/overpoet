package com.overpoet.core;

import java.util.HashSet;
import java.util.Set;

import com.overpoet.core.state.StateStream;
import com.overpoet.actuator.Actuator;
import com.overpoet.sensor.Sensor;
import com.overpoet.spi.Manipulator;
import com.overpoet.spi.Monitor;

public class Engine {

    public Engine() {

    }

    public synchronized  <T> void addSensor(Sensor<T> sensor) {
        SensorHolder<T> wrapped = wrap(sensor);
        this.sensors.add(wrapped);
        for (WrappedMonitor monitor : this.monitors) {
            monitor.register(wrapped.forMonitor(monitor));
        }
    }

    private <T> SensorHolder<T> wrap(Sensor<T> sensor) {
        return new SensorHolder<>(this.state, sensor);
    }

    public synchronized <T> void addActuator(Actuator<T> actuator) {
        ActuatorHolder<T> wrapped = wrap(actuator);
        this.actuators.add(wrapped);
        for (WrappedManipulator manipulator : this.manipulators) {
            manipulator.register(wrapped.forManipulator(manipulator));
        }
    }

    private <T> ActuatorHolder<T> wrap(Actuator<T> actuator) {
        return new ActuatorHolder<>(this.state, actuator);
    }

    public synchronized void connect(Manipulator manipulator) {
        for (ActuatorHolder<?> actuator : this.actuators) {
            manipulator.register(actuator.forManipulator(manipulator));
        }

        this.manipulators.add(wrap(manipulator));
    }

    private WrappedManipulator wrap(Manipulator manipulator) {
        return new WrappedManipulator(manipulator);
    }

    public synchronized void connect(Monitor monitor) {
        for (SensorHolder<?> sensor : this.sensors) {
            monitor.register(sensor.forMonitor(monitor));
        }

        this.monitors.add(wrap(monitor));
    }

    private WrappedMonitor wrap(Monitor monitor) {
        return new WrappedMonitor(monitor);
    }

    private StateStream state = new StateStream();

    private Set<SensorHolder<?>> sensors = new HashSet<>();
    private Set<ActuatorHolder<?>> actuators = new HashSet<>();
    private Set<WrappedManipulator> manipulators = new HashSet<>();
    private Set<WrappedMonitor> monitors = new HashSet<>();
}
