package com.overpoet.core;

import com.overpoet.actuator.Actuator;
import com.overpoet.core.state.StateStream;
import com.overpoet.spi.Manipulator;

class ActuatorHolder<T> {

    ActuatorHolder(StateStream state, Actuator<T> actuator) {
        this.state = state;
        this.actuator = actuator;
    }

    Manipulator.Actuator<T> forManipulator(Manipulator manipulator) {
        return new ManipulatorActuactor(manipulator);
    }

    void actuate(Manipulator manipulator, T value) {
        this.state.add(this.actuator, value);
    }

    private final StateStream state;
    private final Actuator<T> actuator;

    class ManipulatorActuactor implements Manipulator.Actuator<T> {

        ManipulatorActuactor(Manipulator manipulator) {
            this.manipulator = manipulator;
        }

        @Override
        public Class<T> datatype() {
            return ActuatorHolder.this.actuator.datatype();
        }

        @Override
        public void actuate(T value) {
            ActuatorHolder.this.actuate(manipulator, value);
        }

        private final Manipulator manipulator;
    }

}
