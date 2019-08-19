package com.overpoet.core.engine;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.overpoet.core.apparatus.Apparatus;
import com.overpoet.core.engine.state.InMemoryStateStream;
import com.overpoet.core.manipulator.Manipulator;

public class Engine {

    public Engine(EngineConfiguration config) {
        this.config = config;
    }

    EngineConfiguration configuration() {
        return this.config;
    }

    public void start() {
        System.err.println( "start");
        new PlatformManager(this).initialize();
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {

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
        for (ApparatusHolder apparatus : this.apparatuses) {
            manipulator.connect(apparatus.forManipulator(manipulator));
        }

        this.manipulators.add(wrap(manipulator));
    }

    private ManipulatorHolder wrap(Manipulator manipulator) {
        return new ManipulatorHolder(manipulator);
    }

    private final EngineConfiguration config;

    private InMemoryStateStream state = new InMemoryStateStream();

    private Set<ApparatusHolder> apparatuses = new HashSet<>();
    private Set<ManipulatorHolder> manipulators = new HashSet<>();

    private CountDownLatch latch = new CountDownLatch(1);
}
