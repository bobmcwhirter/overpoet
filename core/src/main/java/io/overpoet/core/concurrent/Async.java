package io.overpoet.core.concurrent;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Async {

    static <T> void forEach(ForkJoinPool pool, Stream<T> stream, Consumer<T> action) {
        ForkJoinTask.invokeAll(
                stream
                        .map(e -> ForkJoinTask.adapt(() -> {
                            action.accept(e);
                        }))
                        .collect(Collectors.toList())
        );
    }
}
