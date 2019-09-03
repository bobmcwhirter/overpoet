package io.overpoet.core.engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;

import io.overpoet.core.apparatus.Apparatus;
import io.overpoet.core.concurrent.NamedForkJoinWorkerThreadFactory;
import io.overpoet.core.concurrent.NamedThreadFactory;
import io.overpoet.core.engine.state.InMemoryStateStream;
import io.overpoet.core.manipulator.Manipulator;
import io.overpoet.core.ui.impl.UIManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Engine {
    private static final Logger LOG = LoggerFactory.getLogger("overpoet.core.engine");

    public Engine(EngineConfiguration config) {
        this.config = config;
        this.executor = Executors.newScheduledThreadPool(10, new NamedThreadFactory("core-executor"));
        this.pool = new ForkJoinPool(4,
                                     new NamedForkJoinWorkerThreadFactory("core-pool"),
                                     this::handle,
                                     true);
        this.uiManager = new UIManager(this.pool);
        this.plaformManager = new PlatformManager(this);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("shutting down");
            stop();
        }));
    }

    void handle(Thread thread, Throwable t) {

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
        LOG.info("start up");
        try {
            this.uiManager.start(8080);
            this.plaformManager.initialize();
            LOG.info("start up complete");
            this.latch = new CountDownLatch(1);
            new Thread(() -> {
                try {
                    this.latch.await();
                    this.plaformManager.stop();
                    this.uiManager.stop();
                    this.pool.shutdown();
                    this.executor.shutdown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (this.latch != null) {
            LOG.info("stopping");
            this.latch.countDown();
        }
    }

    synchronized void connect(Apparatus apparatus) {
        ApparatusHolder wrapped = wrap(apparatus);
        this.apparatuses.add(wrapped);
        List<ManipulatorHolder> currentManipulators = new ArrayList<>();
        currentManipulators.addAll(this.manipulators);
        this.pool.submit(
                () -> {
                    for (ManipulatorHolder manipulator : this.manipulators) {
                        manipulator.connect(wrapped);
                    }
                }
        );
    }

    private ApparatusHolder wrap(Apparatus apparatus) {
        return new ApparatusHolder(this.state, apparatus);
    }

    synchronized void connect(Manipulator manipulator) {
        this.manipulators.add(wrap(manipulator));
        ArrayList<ApparatusHolder> currentApparatuses = new ArrayList<>();
        currentApparatuses.addAll(this.apparatuses);
        this.pool.submit(
                () -> {
                    for (ApparatusHolder apparatus : currentApparatuses) {
                        manipulator.connect(apparatus.forManipulator(manipulator));
                    }
                }
        );

    }

    private ManipulatorHolder wrap(Manipulator manipulator) {
        return new ManipulatorHolder(manipulator);
    }

    ForkJoinPool forkJoinPool() {
        return this.pool;
    }

    private final EngineConfiguration config;

    private final ScheduledExecutorService executor;

    private final ForkJoinPool pool;

    private InMemoryStateStream state = new InMemoryStateStream();

    private Set<ApparatusHolder> apparatuses = new HashSet<>();

    private Set<ManipulatorHolder> manipulators = new HashSet<>();

    private CountDownLatch latch;

    private PlatformManager plaformManager;

    private final UIManager uiManager;

}
