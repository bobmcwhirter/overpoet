package io.overpoet.engine.engine;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.Callable;

import io.overpoet.spi.platform.Platform;
import io.overpoet.spi.platform.PlatformContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PlatformHolder {

    private static final Logger LOG = LoggerFactory.getLogger("overpoet.engine.platform");

    PlatformHolder(Path dir, PlatformManager platformManager) throws IOException {
        this.dir = dir;
        this.classLoader = directoryClassLoader(dir.resolve("lib"));
        ServiceLoader<Platform> platformsLoader = ServiceLoader.load(Platform.class, this.classLoader);
        Optional<Platform> optional = platformsLoader.findFirst();
        if (!optional.isPresent()) {
            LOG.warn("no platform discovered in {}", dir.toString());
            this.platform = null;
            this.context = null;
            return;
        }
        this.platform = optional.get();
        this.context = platformManager.contextForPlatform(this.platform);
    }

    Callable<Void> initialize() {
        return withClassLoader(() -> {
            LOG.info("initializing {} [{}]", this.platform.name(), this.platform.id());
            this.platform.initialize(this.context);
        });
    }

    Callable<Void> start() {
        return withClassLoader(() -> {
            LOG.info("starting {} [{}]", this.platform.name(), this.platform.id());
            this.platform.start();
            LOG.info("started {} [{}]", this.platform.name(), this.platform.id());
        });
    }

    Callable<Void> stop() {
        return withClassLoader(() -> {
            LOG.info("stopping {} [{}]", this.platform.name(), this.platform.id());
            try {
                this.platform.stop();
                LOG.info("stopped {} [{}]", this.platform.name(), this.platform.id());
            } catch (InterruptedException e) {
                LOG.warn("stop interrupted {} [{}]: {}", this.platform.name(), this.platform.id(), e.getMessage());
            }
        });
    }

    Callable<Void> withClassLoader(Runnable r) {
        return () -> {

            ClassLoader prev = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(this.classLoader);
                r.run();
            } finally {
                Thread.currentThread().setContextClassLoader(prev);

            }
            return null;
        };
    }

    private ClassLoader directoryClassLoader(Path dir) throws IOException {
        return new URLClassLoader(jars(dir), Thread.currentThread().getContextClassLoader());
    }

    private static URL[] jars(Path dir) throws IOException {
        DirectoryStream<Path> stream = Files.newDirectoryStream(dir);

        List<URL> urls = new ArrayList<>();
        for (Path path : stream) {
            urls.add(path.toUri().toURL());
        }

        return urls.toArray(new URL[0]);
    }

    private final Path dir;

    private final Platform platform;

    private final PlatformContext context;

    private final ClassLoader classLoader;
}
