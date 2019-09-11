package io.overpoet.engine.engine;

import io.overpoet.engine.engine.state.Actuation;
import io.overpoet.engine.engine.state.StateException;
import io.overpoet.engine.engine.state.StateStream;
import io.overpoet.spi.Key;
import io.overpoet.spi.actuator.Actuator;
import io.overpoet.spi.manipulator.Manipulator;

class ActuatorHolder<T> {

    ActuatorHolder(StateStream state, Key key, Actuator<T> actuator) {
        this.state = state;
        this.key = key;
        this.actuator = actuator;
    }

    Actuator<T> forManipulator(Manipulator manipulator) {
        if ( this.actuator == null ) {
            return null;
        }
        ActuatorDelegate delegate = new ActuatorDelegate(manipulator);
        return delegate;
    }

    void actuate(Manipulator manipulator, T value) {
        try {
            this.state.add(new Actuation<>(this.key, value));
            this.actuator.actuate(value);
        } catch (StateException e) {
            e.printStackTrace();
        }
    }

    private final StateStream state;

    private final Actuator<T> actuator;

    private final Key key;

    private class ActuatorDelegate implements Actuator<T> {

        ActuatorDelegate(Manipulator manipulator) {
            this.manipulator = manipulator;
        }

        @Override
        public void actuate(T value) {
            ActuatorHolder.this.actuate(this.manipulator, value);
        }

        private final Manipulator manipulator;
    }

}
