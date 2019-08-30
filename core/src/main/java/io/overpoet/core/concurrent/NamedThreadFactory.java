package io.overpoet.core.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.jetbrains.annotations.NotNull;

public class NamedThreadFactory implements ThreadFactory {

    public NamedThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(@NotNull Runnable r) {
        return new Thread(r, name + "-" + counter.getAndIncrement());
    }

    private final String name;

    private final AtomicInteger counter = new AtomicInteger();
}
