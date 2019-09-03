package io.overpoet.core.concurrent;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedForkJoinWorkerThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory  {

    public NamedForkJoinWorkerThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
            ForkJoinWorkerThread thread = new ForkJoinWorkerThread(pool) { };
            thread.setName(this.name + "-" + counter.getAndIncrement());
            return thread;
    }

    private final String name;
    private AtomicInteger counter = new AtomicInteger();
}
