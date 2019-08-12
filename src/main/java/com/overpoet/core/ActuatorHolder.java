package com.overpoet.core;

import com.overpoet.Keyed;
import com.overpoet.actuator.ActuationException;
import com.overpoet.actuator.Actuator;
import com.overpoet.core.state.StateException;
import com.overpoet.core.state.StateStream;
import com.overpoet.manipulator.Manipulator;

class ActuatorHolder<T> {

    ActuatorHolder(StateStream state, Keyed.Key key, Actuator<T> actuator) {
        this.state = state;
        this.actuator = actuator;
        this.key = key;
    }

    Manipulator.Actuator<T> forManipulator(Manipulator manipulator) {
        return new ManipulatorActuactor(manipulator);
    }

    void actuate(Manipulator manipulator, T value) throws ActuationException {
        try {
            this.state.add(this.actuator, value);
        } catch (StateException e) {
            throw new ActuationException(e);
        }
    }

    private final StateStream state;
    private final Actuator<T> actuator;
    private final Keyed.Key key;

    class ManipulatorActuactor implements Manipulator.Actuator<T> {

        ManipulatorActuactor(Manipulator manipulator) {
            this.manipulator = manipulator;
        }

        @Override
        public String id() {
            return ActuatorHolder.this.actuator.id();
        }

        @Override
        public Key key() {
            return ActuatorHolder.this.key;
        }

        @Override
        public Class<T> datatype() {
            return ActuatorHolder.this.actuator.datatype();
        }

        @Override
        public void actuate(T value) throws ActuationException {
            ActuatorHolder.this.actuate(manipulator, value);
        }

        private final Manipulator manipulator;
    }

}
