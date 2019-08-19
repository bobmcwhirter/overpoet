package com.overpoet.core.engine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import com.overpoet.core.platform.Platform;

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
    public Properties forPlatform(Platform platform) {
        Properties props = new Properties();

        Path propsPath = propsPath(platform);
        System.err.println( "load: " + propsPath);
        if ( Files.exists(propsPath)) {
            if ( Files.isDirectory(propsPath)) {
                throw new IllegalArgumentException("Path " + propsPath + " is not a properties file");
            }
            if ( ! Files.isReadable(propsPath)) {
                throw new IllegalArgumentException("Path " + propsPath + " is not readable");
            }

            try (FileInputStream in = new FileInputStream(propsPath.toFile())) {
                props.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return props;
    }

    private Path propsPath(Platform platform) {
        return this.dir.resolve( platform.getClass().getPackage().getName() + ".properties");
    }

    private final Path dir;
}
