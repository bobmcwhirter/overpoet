package io.overpoet.core.engine;

import io.overpoet.core.apparatus.Apparatus;
import io.overpoet.core.manipulator.Manipulator;

class ManipulatorHolder {

    ManipulatorHolder(Manipulator manipulator) {
        this.manipulator = manipulator;
    }

    public void connect(ApparatusHolder apparatus) {
        Apparatus forManipulator = apparatus.forManipulator(this.manipulator);
        if ( forManipulator != null ) {
            this.manipulator.connect(forManipulator);
        }
    }

    private final Manipulator manipulator;
}
