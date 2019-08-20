package com.overpoet.core.engine.state;

import java.util.HashMap;
import java.util.Map;

import com.overpoet.core.actuator.Actuator;
import com.overpoet.core.sensor.Sensor;

public class InMemoryState implements State {

    static InMemoryState NIL = new InMemoryState();

    private InMemoryState() {
        this.previousState = null;
    }

    private InMemoryState(InMemoryState previousState) {
        this.previousState = previousState;
        this.senses.putAll(previousState.senses);
        this.actuations.putAll(previousState.actuations);
    }

    <T> InMemoryState(InMemoryState previousState, Sense<T> sense) {
        this(previousState);
        this.senses.put(sense.sensor(), sense.value());
    }

    <T> InMemoryState(InMemoryState previousState, Actuation<T> actuation) {
        this(previousState);
        this.actuations.put(actuation.actuator(), actuation.value());
    }

    @Override
    public synchronized <T> InMemoryState add(Sense<T> sense) throws StateException {
        if (this.locked) {
            throw new StateException("state locked, branches are not allowed");
        }
        Object prevValue = this.senses.get(sense.sensor());
        if (prevValue == null || !prevValue.equals(sense.value())) {
            System.err.println("change: " + sense);
            try {
                return new InMemoryState(this, sense);
            } finally {
                if (this != NIL) {
                    this.locked = true;
                }
            }
        }
        return this;
    }

    @Override
    public synchronized <T> InMemoryState add(Actuation<T> actuation) throws StateException {
        if (this.locked) {
            throw new StateException("state locked, branches are not allowed");
        }
        try {
            return new InMemoryState(this, actuation);
        } finally {
            if (this != NIL) {
                this.locked = true;
            }
        }
    }

    @Override
    public <T> T value(Sensor<T> sensor) {
        return (T) this.senses.get(sensor);
    }

    private boolean locked;

    private final InMemoryState previousState;

    private Map<Sensor<?>, Object> senses = new HashMap<>();

    private Map<Actuator<?>, Object> actuations = new HashMap<>();
}
