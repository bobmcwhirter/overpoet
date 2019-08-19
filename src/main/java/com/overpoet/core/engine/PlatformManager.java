package com.overpoet.core.engine;

import java.util.Properties;
import java.util.ServiceLoader;

import com.overpoet.core.apparatus.Apparatus;
import com.overpoet.core.manipulator.Manipulator;
import com.overpoet.core.platform.Platform;
import com.overpoet.core.platform.PlatformContext;

class PlatformManager {

    PlatformManager(Engine engine) {
        this.engine = engine;
    }

    void initialize() {
        System.err.println( "initialize platforms");
        ServiceLoader<Platform> platforms = ServiceLoader.load(Platform.class);

        for (Platform platform : platforms) {
            System.err.println( "--> " + platform);
            Properties config = this.engine.configuration().configurationProvider().forPlatform(platform);
            platform.configure( new Context(config) );
        }
        System.err.println( "done");
    }

    private final Engine engine;

    private class Context implements PlatformContext {

        Context(Properties configuration) {
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
        public Properties configuration() {
            return this.configuration;
        }

        private final Properties configuration;
    }
}
