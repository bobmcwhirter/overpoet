package io.overpoet.core.engine;

import java.util.ServiceLoader;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import io.overpoet.core.apparatus.Apparatus;
import io.overpoet.core.concurrent.Async;
import io.overpoet.core.manipulator.Manipulator;
import io.overpoet.core.platform.Platform;
import io.overpoet.core.platform.PlatformConfiguration;
import io.overpoet.core.platform.PlatformContext;

class PlatformManager {

    PlatformManager(Engine engine) {
        this.engine = engine;
    }

    void initialize() {
        ServiceLoader<Platform> platforms = ServiceLoader.load(Platform.class);

        Async.forEach(platforms.stream().map(ServiceLoader.Provider::get),
                      e -> {
                          PlatformConfiguration config = this.engine.configuration().configurationProvider().forPlatform(e);
                          e.configure(new Context(config));
                      });
    }

    private final Engine engine;

    private class Context implements PlatformContext {

        Context(PlatformConfiguration configuration) {
            this.configuration = configuration;
        }

        @Override
        public void connect(Apparatus apparatus) {
            PlatformManager.this.engine.connect(apparatus);
        }

        @Override
        public void connect(Manipulator manipulator) {
            PlatformManager.this.engine.connect(manipulator);
        }

        @Override
        public PlatformConfiguration configuration() {
            return this.configuration;
        }

        @Override
        public ScheduledExecutorService executor() {
            return PlatformManager.this.engine.executor();
        }

        private final PlatformConfiguration configuration;
    }
}
