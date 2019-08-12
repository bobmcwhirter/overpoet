package com.overpoet.core;


import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.overpoet.Keyed;
import com.overpoet.sensor.SenseException;
import com.overpoet.core.state.StateException;
import com.overpoet.core.state.StateStream;
import com.overpoet.sensor.Sensor;
import com.overpoet.manipulator.Manipulator;

class SensorHolder<T> implements Sensor.Sink<T> {

    SensorHolder(StateStream state, Keyed.Key key, Sensor<T> sensor) {
        this.state = state;
        this.sensor = sensor;
        this.sensor.initialize(this);
        this.key = key;
    }

    @Override
    public void sink(T value) throws SenseException {
        try {
            this.state.add(this.sensor, value);
            for (ManipulatorSensor monitor : this.monitors) {
                monitor.accept(value);
            }
        } catch (StateException e) {
            throw new SenseException(e);
        }
    }

    Class<T> datatype() {
        return this.sensor.datatype();
    }

    Manipulator.Sensor<T> forManipulator(Manipulator monitor) {
        ManipulatorSensor manipulatorSensor = new ManipulatorSensor(monitor);
        this.monitors.add(manipulatorSensor);
        return manipulatorSensor;
    }

    private final StateStream state;
    private final Sensor<T> sensor;
    private final Set<ManipulatorSensor> monitors = new HashSet<>();
    private final Keyed.Key key;

    class ManipulatorSensor implements Manipulator.Sensor<T>, Consumer<T> {

        ManipulatorSensor(Manipulator manipulator) {
            this.manipulator = manipulator;
        }

        @Override
        public String id() {
            return SensorHolder.this.sensor.id();
        }

        @Override
        public Key key() {
            return SensorHolder.this.key;
        }

        @Override
        public Class<T> datatype() {
            return SensorHolder.this.sensor.datatype();
        }

        @Override
        public void onChange(Consumer<T> listener) {
            this.listener = listener;
        }

        @Override
        public void accept(T t) {
            if ( this.listener != null) {
                this.listener.accept(t);
            }
        }

        private final Manipulator manipulator;
        private Consumer<T> listener = null;
    }
}
