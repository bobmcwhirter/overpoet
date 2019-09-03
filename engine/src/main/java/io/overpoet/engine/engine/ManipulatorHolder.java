package io.overpoet.engine.engine;

import io.overpoet.spi.apparatus.Apparatus;
import io.overpoet.spi.manipulator.Manipulator;

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
