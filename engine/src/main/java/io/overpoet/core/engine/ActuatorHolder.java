package io.overpoet.core.engine;

import io.overpoet.spi.Key;
import io.overpoet.spi.actuator.ActuationException;
import io.overpoet.spi.actuator.Actuator;
import io.overpoet.core.engine.state.Actuation;
import io.overpoet.core.engine.state.StateException;
import io.overpoet.core.engine.state.InMemoryStateStream;
import io.overpoet.spi.manipulator.Manipulator;

class ActuatorHolder<T> {

    ActuatorHolder(InMemoryStateStream state, Actuator<T> actuator) {
        this.state = state;
        this.actuator = actuator;
    }

    Actuator<T> forManipulator(Manipulator manipulator) {
        return new ManipulatorActuator(manipulator);
    }

    void actuate(Manipulator manipulator, T value) throws ActuationException {
        try {
            this.state.add( new Actuation<>(this.actuator, value));
        } catch (StateException e) {
            throw new ActuationException(e);
        }
    }

    private final InMemoryStateStream state;
    private final Actuator<T> actuator;

    private class ManipulatorActuator implements Actuator<T> {

        ManipulatorActuator(Manipulator manipulator) {
            this.manipulator = manipulator;
        }

        @Override
        public Key key() {
            return ActuatorHolder.this.actuator.key();
        }

        @Override
        public Class<T> datatype() {
            return ActuatorHolder.this.actuator.datatype();
        }

        @Override
        public void actuate(T value) throws ActuationException {
            ActuatorHolder.this.actuate(this.manipulator, value);
        }

        private final Manipulator manipulator;
    }

}
