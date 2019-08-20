package io.overpoet.core.engine;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import io.overpoet.Key;
import io.overpoet.core.engine.state.InMemoryStateStream;
import io.overpoet.core.engine.state.Sense;
import io.overpoet.core.engine.state.StateException;
import io.overpoet.core.engine.state.StateStream;
import io.overpoet.core.manipulator.Manipulator;
import io.overpoet.core.sensor.Sensor;
import io.overpoet.core.sensor.SensorLogic;
import io.overpoet.core.sensor.Sink;

class SensorHolder<T> {

    SensorHolder(InMemoryStateStream state, Sensor<T> sensor) {
        this.state = state;
        this.sensor = sensor;
        this.sensor.onChange(this::sink);
    }

    public void sink(T value) {
        try {
            boolean changed = this.state.add(new Sense<>(this.sensor, value));
            if ( changed ) {
                for (ManipulatorSensorLogic each : this.logics) {
                    each.delegate(value);
                }
            }
        } catch (StateException e) {
            e.printStackTrace();
        }
    }

    Class<T> datatype() {
        return this.sensor.datatype();
    }

    Sensor<T> forManipulator(Manipulator manipulator) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<? extends Sensor> ctor = this.sensor.getClass().getDeclaredConstructor(Key.class, this.sensor.metadata().getClass(), SensorLogic.class);
        ManipulatorSensorLogic logic = new ManipulatorSensorLogic();
        Sensor manipulatorSensor = ctor.newInstance(this.sensor.key(), this.sensor.metadata(), logic);
        this.manipulatorSensors.add(manipulatorSensor);
        this.logics.add( logic );
        return manipulatorSensor;
    }

    private final StateStream state;

    private final Sensor<T> sensor;

    private final Set<Sensor<?>> manipulatorSensors = new HashSet<>();
    private final Set<ManipulatorSensorLogic<?>> logics  = new HashSet<>();

    private static class ManipulatorSensorLogic<T> implements SensorLogic<T> {
        void delegate(T value) {
            this.sink.sink(value);
        }

        @Override
        public void start(Sink<T> sink) {
            this.sink = sink;
        }

        private Sink<T> sink;
    }

}
