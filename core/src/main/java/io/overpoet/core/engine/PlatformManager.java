package io.overpoet.core.engine;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ScheduledExecutorService;

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

        Map<Platform, Context> init = new HashMap<>();

        platforms.stream().map(ServiceLoader.Provider::get).forEach(e->{
            PlatformConfiguration config = this.engine.configuration().configurationProvider().forPlatform(e);
            init.put( e, new Context(config));
        });

        System.err.println( "initialize platforms");
        Async.forEach(init.keySet().stream(), (p)->{
            Context c = init.get(p);
            p.initialize(c);
        });

        System.err.println( "start platforms");
        Async.forEach(init.keySet().stream(), (p)->{
            p.start();
        });
        System.err.println( "started");
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
