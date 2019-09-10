package io.overpoet.engine.engine;


import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import io.overpoet.engine.engine.state.Sense;
import io.overpoet.engine.engine.state.StateException;
import io.overpoet.engine.engine.state.StateStream;
import io.overpoet.spi.Key;
import io.overpoet.spi.manipulator.Manipulator;
import io.overpoet.spi.sensor.Sensor;
import io.overpoet.spi.sensor.Sink;

class SensorHolder<T> {

    SensorHolder(StateStream state, Key key, Sensor<T> sensor) {
        this.state = state;
        this.key = key;
        this.sensor = sensor;
        if ( this.sensor != null ){
            this.sensor.onChange(this::sink);
        }
    }

    public void sink(T value) {
        try {
            boolean changed = this.state.add(new Sense<>(this.key, value));
            if ( changed ) {
                System.err.println( "holder " + key + " setLastValue: " + value);
                this.lastValue = value;
                for (SensorDelegate each : this.delegates) {
                    each.delegate(value);
                }
            }
        } catch (StateException e) {
            e.printStackTrace();
        }
    }


    Sensor<T> forManipulator(Manipulator manipulator) {
        if ( this.sensor == null ) {
            return null;
        }
        SensorDelegate delegate = new SensorDelegate(manipulator);
        this.delegates.add(delegate);
        delegate.delegate(this.lastValue);
        return delegate;
    }

    private final StateStream state;

    private final Sensor<T> sensor;

    private final Set<SensorDelegate<?>> delegates = new HashSet<>();

    private final Key key;

    private T lastValue;

    private static class SensorDelegate<T> implements Sensor<T> {
        SensorDelegate(Manipulator manipulator) {
            this.manipulator = manipulator;
        }

        void delegate(T value) {
            this.lastValue = value;
            if ( this.sink != null ) {
                this.sink.sink(value);
            }
        }

        @Override
        public void onChange(Sink<T> sink) {
            this.sink = sink;
            if ( this.sink != null && this.lastValue != null ) {
                this.sink.sink(this.lastValue);
            }
        }

        private final Manipulator manipulator;

        private Sink<T> sink;

        private T lastValue;
    }

}
