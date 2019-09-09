package io.overpoet.engine.engine;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import io.overpoet.spi.apparatus.Apparatus;
import io.overpoet.spi.manipulator.Manipulator;
import io.overpoet.spi.platform.Platform;
import io.overpoet.spi.platform.PlatformConfiguration;
import io.overpoet.spi.platform.PlatformContext;
import io.overpoet.spi.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PlatformManager {

    private static final Logger LOG = LoggerFactory.getLogger("overpoet.core.platform");

    PlatformManager(Engine engine) {
        this.engine = engine;
    }

    void initialize(Path root) throws IOException, ExecutionException, InterruptedException {
        Files.walkFileTree(root, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (dir.getParent().getParent().equals(root)) {
                    if (Files.exists(dir.resolve("platform.properties"))) {
                        PlatformManager.this.platforms.add(new PlatformHolder(dir, PlatformManager.this));
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });

        initialize();
    }

    private void initialize() throws ExecutionException, InterruptedException {
        LOG.info("Initialize platforms");
        await( this.engine.forkJoinPool().invokeAll(
                this.platforms.stream().map(PlatformHolder::initialize)
                        .collect(Collectors.toList())
        ) );

        await( this.engine.forkJoinPool().invokeAll(
                this.platforms.stream().map(PlatformHolder::start)
                        .collect(Collectors.toList())
        )) ;
        LOG.info("Initialize platforms complete");
    }

    private void await(List<Future<Void>> all) throws ExecutionException, InterruptedException {
        for (Future<Void> each : all) {
            each.get();
        }
    }


    void stop() {
            this.engine.forkJoinPool().invokeAll(
                this.platforms.stream().map(PlatformHolder::stop)
                        .collect(Collectors.toList())
        );
            /*
        this.engine.forkJoinPool().invokeAll(this.platforms.keySet().stream()
                                                     .map(p -> (Callable<Void>) () -> {
                                                         LOG.info("stopping {} [{}]", p.name(), p.id());
                                                         p.stop();
                                                         return null;
                                                     })
                                                     .collect(Collectors.toList()));
             */
    }

    PlatformContext contextForPlatform(String platformId) {
        return new Context(configurationForPlatform(platformId), uiForPlatform(platformId));
    }

    PlatformConfiguration configurationForPlatform(String platformId) {
        return this.engine.configuration().configurationProvider().forPlatform(platformId);

    }

    UI uiForPlatform(String platformId) {
        return this.engine.uiManager().forPlatform(platformId);
    }

    private final Engine engine;

    //private final Map<Platform, Context> platforms = new HashMap<>();
    private final List<PlatformHolder> platforms = new ArrayList<>();

    class Context implements PlatformContext {

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
