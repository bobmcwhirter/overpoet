package com.overpoet.core.engine.state;

import com.overpoet.core.Actuation;
import com.overpoet.core.Sense;
import com.overpoet.core.actuator.Actuator;
import com.overpoet.core.sensor.Sensor;

public class StateStream {

    private State currentHead = State.NIL;

    public StateStream() {

    }

    public State currentHead() {
        return this.currentHead;
    }

    public <T> StateStream add(Sensor<T> sensor, T value) throws StateException {
        return add(new SimpleSense<>(sensor, value));
    }

    private synchronized <T> StateStream add(Sense<T> sense) throws StateException {
        this.currentHead = this.currentHead.add(sense);
        return this;
    }

    public <T> StateStream add(Actuator<T> actuator, T value) throws StateException {
        return add(new SimpleActuation<>(actuator, value));
    }

    private synchronized <T> StateStream add(Actuation<T> actuation) throws StateException {
        this.currentHead = this.currentHead.add(actuation);
        return this;
    }
}
