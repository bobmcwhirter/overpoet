package com.overpoet.core.engine;

import java.util.HashSet;
import java.util.Set;

import com.overpoet.core.config.Configurable;
import com.overpoet.core.config.Configuration;
import com.overpoet.core.apparatus.Apparatus;
import com.overpoet.core.engine.state.InMemoryStateStream;
import com.overpoet.core.manipulator.Manipulator;

public class Engine {

    public Engine(Configuration config) {
        this.config = config;
    }

    public synchronized void connect(Apparatus apparatus) {
        ApparatusHolder wrapped = wrap(apparatus);
        this.apparatuses.add(wrapped);

        for (ManipulatorHolder manipulator : this.manipulators) {
            manipulator.connect(wrapped);
        }
    }

    private ApparatusHolder wrap(Apparatus apparatus) {
        return new ApparatusHolder(this.state, apparatus);
    }

    public synchronized void connect(Manipulator manipulator) {
        if ( manipulator instanceof Configurable ) {
            ((Configurable) manipulator).configure(this.config);
        }
        for (ApparatusHolder apparatus : this.apparatuses) {
            manipulator.connect(apparatus.forManipulator(manipulator));
        }

        this.manipulators.add(wrap(manipulator));
    }

    private ManipulatorHolder wrap(Manipulator manipulator) {
        return new ManipulatorHolder(manipulator);
    }

    private final Configuration config;

    private InMemoryStateStream state = new InMemoryStateStream();

    private Set<ApparatusHolder> apparatuses = new HashSet<>();
    private Set<ManipulatorHolder> manipulators = new HashSet<>();
}
