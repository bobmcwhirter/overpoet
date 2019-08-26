package io.overpoet.core.engine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.Properties;
import java.util.Set;

import io.overpoet.core.platform.PlatformConfiguration;

public class FilesystemPlatformConfiguration implements PlatformConfiguration  {

    public FilesystemPlatformConfiguration(Path file) {
        this.file = file;
        this.props = new Properties();
        if (Files.exists(file)) {
            load();
        } else {
            save();
        }
    }

    private void load() {
        try (InputStream in = new FileInputStream(this.file.toFile())) {
            this.props.clear();
            this.props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try (OutputStream out = new FileOutputStream(this.file.toFile())) {
            this.props.store(out, "created by overpoet at " + ZonedDateTime.now());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void set(String key, String value) {
        this.props.setProperty(key, value);
        save();
    }

    @Override
    public String get(String key) {
        return this.props.getProperty(key);
    }

    @Override
    public void remove(String key) {
        this.props.remove(key);
        save();
    }

    @Override
    public Set<String> keys() {
        return this.props.stringPropertyNames();
    }

    private final Path file;
    private final Properties props;
}
