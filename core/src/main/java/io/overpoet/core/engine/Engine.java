package io.overpoet.core.engine;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import io.overpoet.core.apparatus.Apparatus;
import io.overpoet.core.engine.state.InMemoryStateStream;
import io.overpoet.core.manipulator.Manipulator;
import io.overpoet.core.ui.impl.UIManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Engine {
    private static final Logger LOG = LoggerFactory.getLogger(Engine.class);

    public Engine(EngineConfiguration config) {
        this.config = config;
        this.executor = Executors.newScheduledThreadPool(10);
        this.uiManager = new UIManager();
        this.plaformManager = new PlatformManager(this);
    }

    EngineConfiguration configuration() {
        return this.config;
    }

    ScheduledExecutorService executor() {
        return this.executor;
    }

    UIManager uiManager() {
        return this.uiManager;
    }

    public void start() {
        LOG.info("Starting");
        try {
            this.uiManager.start(8080);
            //this.plaformManager.initialize();;
            this.latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {

    }

    synchronized void connect(Apparatus apparatus) {
        ApparatusHolder wrapped = wrap(apparatus);
        this.apparatuses.add(wrapped);

        for (ManipulatorHolder manipulator : this.manipulators) {
            manipulator.connect(wrapped);
        }
    }

    private ApparatusHolder wrap(Apparatus apparatus) {
        return new ApparatusHolder(this.state, apparatus);
    }

    synchronized void connect(Manipulator manipulator) {
        for (ApparatusHolder apparatus : this.apparatuses) {
            manipulator.connect(apparatus.forManipulator(manipulator));
        }

        this.manipulators.add(wrap(manipulator));
    }

    private ManipulatorHolder wrap(Manipulator manipulator) {
        return new ManipulatorHolder(manipulator);
    }

    private final EngineConfiguration config;

    private final ScheduledExecutorService executor;

    private InMemoryStateStream state = new InMemoryStateStream();

    private Set<ApparatusHolder> apparatuses = new HashSet<>();
    private Set<ManipulatorHolder> manipulators = new HashSet<>();

    private CountDownLatch latch = new CountDownLatch(1);

    private PlatformManager plaformManager;
    private final UIManager uiManager;

}
