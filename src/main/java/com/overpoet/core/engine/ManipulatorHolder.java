package com.overpoet.core.engine;

import com.overpoet.core.manipulator.Manipulator;

class ManipulatorHolder {

    ManipulatorHolder(Manipulator manipulator) {
        this.manipulator = manipulator;
    }

    public void register(ApparatusHolder apparatus) {
        Manipulator.Apparatus forManipulator = apparatus.forManipulator(this.manipulator);
        if ( forManipulator != null ) {
            this.manipulator.register(forManipulator);
        }
    }

    private final Manipulator manipulator;
}
