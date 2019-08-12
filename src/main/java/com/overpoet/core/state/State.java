package com.overpoet.core.state;

import java.util.HashMap;
import java.util.Map;

import com.overpoet.actuator.Actuator;
import com.overpoet.core.Actuation;
import com.overpoet.core.Sense;
import com.overpoet.sensor.Sensor;

public class State {

    public static final State NIL = new State();

    private State() {
        this.previousState = null;
    }

    private State(State previousState) {
        this.previousState = previousState;
        this.senses.putAll(previousState.senses);
        this.actuations.putAll(previousState.actuations);
    }

    <T> State(State previousState, Sense<T> sense) {
        this(previousState);
        this.senses.put(sense.sensor(), sense.value());
    }

    <T> State(State previousState, Actuation<T> actuation) {
        this(previousState);
        this.actuations.put(actuation.actuator(), actuation.value());
    }

    public synchronized  <T> State add(Sense<T> sense) throws StateException {
        if ( this.locked ) {
            throw new StateException("state locked, branches are not allowed");
        }
        try {
            return new State(this, sense);
        } finally {
            if ( this != NIL ) {
                this.locked = true;
            }
        }
    }

    public synchronized  <T> State add(Actuation<T> actuation) throws StateException {
        if ( this.locked ) {
            throw new StateException("state locked, branches are not allowed");
        }
        try {
            return new State(this, actuation);
        } finally {
            if ( this != NIL ) {
                this.locked = true;
            }
        }
    }

    public <T> T value(Sensor<T> sensor) {
        return (T) this.senses.get(sensor);
    }

    private boolean locked;
    private final State previousState;
    private Map<Sensor<?>, Object> senses = new HashMap<>();
    private Map<Actuator<?>, Object> actuations = new HashMap<>();
}
