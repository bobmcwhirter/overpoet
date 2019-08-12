package com.overpoet.core.state;

import com.overpoet.core.Actuation;
import com.overpoet.core.Sense;
import com.overpoet.actuator.Actuator;
import com.overpoet.sensor.Sensor;

public class StateStream {

    private State currentHead = State.NIL;

    public StateStream() {

    }

    public State currentHead() {
        return this.currentHead;
    }

    public <T> StateStream add(Sensor<T> sensor, T value) {
        return add(new Sense<T>() {
            @Override
            public Sensor<T> sensor() {
                return sensor;
            }

            @Override
            public T value() {
                return value;
            }
        });
    }

    private synchronized <T> StateStream add(Sense<T> sense) {
        this.currentHead = this.currentHead.add(sense);
        return this;
    }

    public <T> StateStream add(Actuator<T> actuator, T value) {
        return add(new Actuation<T>() {
            @Override
            public Actuator<T> actuator() {
                return actuator;
            }

            @Override
            public T value() {
                return value;
            }
        });
    }

    private synchronized <T> StateStream add(Actuation<T> actuation) {
        this.currentHead = this.currentHead.add(actuation);
        return this;
    }
}
