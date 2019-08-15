package com.overpoet.core.engine;

import com.overpoet.core.apparatus.Apparatus;
import com.overpoet.core.manipulator.Manipulator;

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
