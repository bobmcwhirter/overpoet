package io.overpoet.boot;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Throwable {
        String homeProp = System.getProperty("overpoet.home");
        Path home = Paths.get(homeProp);
        System.err.println( "boot from " + home);

        Path lib = home.resolve("lib");
        Path libCommon = lib.resolve("common");

        ClassLoader commonClassLoader = directoryClassLoader(libCommon, ClassLoader.getSystemClassLoader());
        ClassLoader engineClassLoader = directoryClassLoader(lib, commonClassLoader);

        Thread.currentThread().setContextClassLoader(engineClassLoader);

        Class<?> cls = engineClassLoader.loadClass("io.overpoet.engine.Main");

        Method entry = cls.getDeclaredMethod("main", String[].class);
        entry.invoke(null, new Object[] {args});
    }

    private static ClassLoader directoryClassLoader(Path dir, ClassLoader parent) throws IOException {
        return new URLClassLoader(jars(dir), parent);
    }

    private  static URL[] jars(Path dir) throws IOException {
        DirectoryStream<Path> stream = Files.newDirectoryStream(dir);

        List<URL> urls = new ArrayList<>();
        for (Path path : stream) {
            urls.add( path.toUri().toURL());
        }

        return urls.toArray(new URL[0]);
    }
}
