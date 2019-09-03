package io.overpoet.spi.platform;

public interface Platform {

    String id();
    String name();

    default void initialize(PlatformContext context) {

    }

    default void start() {

    }

    default void stop() throws InterruptedException {

    }
}
