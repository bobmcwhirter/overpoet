package io.overpoet.core.engine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.overpoet.spi.platform.Platform;
import io.overpoet.spi.platform.PlatformConfiguration;

public class FilesystemConfigurationProvider implements ConfigurationProvider {

    public FilesystemConfigurationProvider(Path dir) {
        if (!Files.exists(dir)) {
            throw new IllegalArgumentException("Path " + dir + " does not exist");
        }
        if ( !Files.isDirectory(dir)) {
            throw new IllegalArgumentException("Path " + dir + " is not a directory");
        }
        if ( !Files.isReadable(dir)) {
            throw new IllegalArgumentException("Path " + dir + " is not readable");
        }

        this.dir = dir;
    }

    @Override
    public PlatformConfiguration forPlatform(Platform platform) {
        Path propsPath = propsPath(platform);
        if ( Files.exists(propsPath)) {
            if ( Files.isDirectory(propsPath)) {
                throw new IllegalArgumentException("Path " + propsPath + " is not a properties file");
            }
            if ( ! Files.isReadable(propsPath)) {
                throw new IllegalArgumentException("Path " + propsPath + " is not readable");
            }

        } else {
            try {
                Files.createDirectories(propsPath.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new FilesystemPlatformConfiguration(propsPath);
    }

    private Path propsPath(Platform platform) {
        return this.dir.resolve( platform.getClass().getPackage().getName() + ".properties");
    }

    private final Path dir;

}
