package com.overpoet.core;

import com.overpoet.manipulator.Manipulator;

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
