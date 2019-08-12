package com.overpoet.core;

import java.util.HashSet;
import java.util.Set;

import com.overpoet.apparatus.Apparatus;
import com.overpoet.core.state.StateStream;
import com.overpoet.manipulator.Manipulator;

public class Engine {

    public Engine() {

    }

    public synchronized void connect(Apparatus apparatus) {
        ApparatusHolder wrapped = wrap(apparatus);
        this.apparatuses.add(wrapped);

        for (ManipulatorHolder manipulator : this.manipulators) {
            manipulator.register(wrapped);
        }
    }

    private ApparatusHolder wrap(Apparatus apparatus) {
        return new ApparatusHolder(this.state, apparatus);
    }

    public synchronized void connect(Manipulator manipulator) {
        for (ApparatusHolder apparatus : this.apparatuses) {
            manipulator.register(apparatus.forManipulator(manipulator));
        }

        this.manipulators.add(wrap(manipulator));
    }

    private ManipulatorHolder wrap(Manipulator manipulator) {
        return new ManipulatorHolder(manipulator);
    }


    private StateStream state = new StateStream();

    private Set<ApparatusHolder> apparatuses = new HashSet<>();
    private Set<ManipulatorHolder> manipulators = new HashSet<>();
}
