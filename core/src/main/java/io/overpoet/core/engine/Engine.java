package io.overpoet.core.engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import io.overpoet.core.apparatus.Apparatus;
import io.overpoet.core.engine.state.InMemoryStateStream;
import io.overpoet.core.manipulator.Manipulator;
import io.overpoet.core.ui.impl.UIManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Engine {
    private static final Logger LOG = LoggerFactory.getLogger("overpoet.core.engine");

    public Engine(EngineConfiguration config) {
        this.config = config;
        this.executor = Executors.newScheduledThreadPool(10, new ThreadFactory() {
            private AtomicInteger counter = new AtomicInteger();

            @Override
            public Thread newThread(@NotNull Runnable r) {
                return new Thread(r, "core-executor-" + counter.getAndIncrement());
            }
        });

        ForkJoinPool.ForkJoinWorkerThreadFactory threadFactory = new ForkJoinPool.ForkJoinWorkerThreadFactory() {
            private AtomicInteger counter = new AtomicInteger();

            @Override
            public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
                ForkJoinWorkerThread thread = new ForkJoinWorkerThread(pool) {
                };
                thread.setName("core-pool-" + counter.getAndIncrement());
                return thread;
            }
        };
        this.pool = new ForkJoinPool(4, threadFactory, this::handle, true);

        this.uiManager = new UIManager();
        this.plaformManager = new PlatformManager(this);
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
            ;
            LOG.info("start up complete");
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

    private CountDownLatch latch = new CountDownLatch(1);

    private PlatformManager plaformManager;

    private final UIManager uiManager;

}
