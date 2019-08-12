package com.overpoet.core;


import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.overpoet.core.state.StateStream;
import com.overpoet.sensor.Sensor;
import com.overpoet.spi.Monitor;

class SensorHolder<T> implements Sensor.Sink<T> {

    SensorHolder(StateStream state, Sensor<T> sensor) {
        this.state = state;
        this.sensor = sensor;
        this.sensor.initialize(this);
    }

    @Override
    public void sink(T value) {
        this.state.add(this.sensor, value);
        for (MonitorSensor monitor : this.monitors) {
            monitor.accept(value);
        }
    }

    Class<T> datatype() {
        return this.sensor.datatype();
    }

    Monitor.Sensor<T> forMonitor(Monitor monitor) {
        MonitorSensor monitorSensor = new MonitorSensor(monitor);
        this.monitors.add(monitorSensor);
        return monitorSensor;
    }

    private final StateStream state;
    private final Sensor<T> sensor;
    private final Set<MonitorSensor> monitors = new HashSet<>();

    class MonitorSensor implements Monitor.Sensor<T>, Consumer<T> {

        MonitorSensor(Monitor monitor) {
            this.monitor = monitor;
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

        private final Monitor monitor;
        private Consumer<T> listener = null;
    }
}
