package io.overpoet.core.engine;

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
import io.overpoet.core.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PlatformManager {

    private static final Logger LOG = LoggerFactory.getLogger(PlatformManager.class);

    PlatformManager(Engine engine) {
        this.engine = engine;
    }

    void initialize() {
        ServiceLoader<Platform> platforms = ServiceLoader.load(Platform.class);

        Map<Platform, Context> init = new HashMap<>();

        platforms.stream().map(ServiceLoader.Provider::get).forEach(e->{
            PlatformConfiguration config = this.engine.configuration().configurationProvider().forPlatform(e);
            init.put( e, new Context(config, engine.uiManager().forPlatform(e)));
        });

        Async.forEach(init.keySet().stream(), (p)->{
            Context c = init.get(p);
            LOG.info("Initializing {} [{}]", p.name(), p.id());
            p.initialize(c);
        });

        Async.forEach(init.keySet().stream(), (p)->{
            LOG.info("Starting {} [{}]", p.name(), p.id());
            p.start();
        });
    }

    private final Engine engine;

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
