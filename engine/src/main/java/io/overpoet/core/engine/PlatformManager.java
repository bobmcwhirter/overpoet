package io.overpoet.core.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import io.overpoet.core.apparatus.Apparatus;
import io.overpoet.core.manipulator.Manipulator;
import io.overpoet.core.platform.Platform;
import io.overpoet.core.platform.PlatformConfiguration;
import io.overpoet.core.platform.PlatformContext;
import io.overpoet.core.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PlatformManager {

    private static final Logger LOG = LoggerFactory.getLogger("overpoet.core.platform");

    PlatformManager(Engine engine) {
        this.engine = engine;
    }


    void initialize() {
        ServiceLoader<Platform> platformsLoader = ServiceLoader.load(Platform.class);

        platformsLoader.stream().map(ServiceLoader.Provider::get).forEach(e -> {
            PlatformConfiguration config = this.engine.configuration().configurationProvider().forPlatform(e);
            this.platforms.put(e, new Context(config, engine.uiManager().forPlatform(e)));
        });

        this.engine.forkJoinPool().invokeAll(this.platforms.keySet().stream()
                               .map(p -> (Callable<Void>) () -> {
                                   Context c = this.platforms.get(p);
                                   LOG.info("initializing {} [{}]", p.name(), p.id());
                                   p.initialize(c);
                                   return null;
                               })
                               .collect(Collectors.toList()));

        this.engine.forkJoinPool().invokeAll(this.platforms.keySet().stream()
                               .map(p -> (Callable<Void>) () -> {
                                   LOG.info("starting up {} [{}]", p.name(), p.id());
                                   p.start();
                                   return null;
                               })
                               .collect(Collectors.toList()));
    }

    void stop() {
        this.engine.forkJoinPool().invokeAll(this.platforms.keySet().stream()
                                                     .map(p -> (Callable<Void>) () -> {
                                                         LOG.info("stopping {} [{}]", p.name(), p.id());
                                                         p.stop();
                                                         return null;
                                                     })
                                                     .collect(Collectors.toList()));
    }

    private final Engine engine;
    private final Map<Platform, Context> platforms = new HashMap<>();



    private class Context implements PlatformContext {

        Context(PlatformConfiguration configuration, UI ui) {
            this.configuration = configuration;
            this.ui = ui;
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

        @Override
        public UI ui() {
            return this.ui;
        }

        private final PlatformConfiguration configuration;

        private final UI ui;
    }
}
