package io.overpoet.engine;

import java.nio.file.Path;
import java.time.ZoneId;

import io.overpoet.engine.engine.ConfigurationProvider;
import io.overpoet.engine.engine.Engine;
import io.overpoet.engine.engine.EngineConfiguration;
import io.overpoet.engine.engine.FilesystemConfigurationProvider;
import io.overpoet.engine.engine.SimpleEngineConfiguration;
import io.overpoet.spi.geo.Latitude;
import io.overpoet.spi.geo.Location;
import io.overpoet.spi.geo.Longitude;
import io.overpoet.spi.geo.Point;
import io.overpoet.spi.measurement.Distance;

import static java.time.ZoneId.of;

public class Main {

    public static void main(String[] args) {
        Location location = new Location() {
            @Override
            public Point point() {
                return new Point(Latitude.north(36.57), Longitude.west(81.05));
            }

            @Override
            public ZoneId getZoneId() {
                return of("America/New_York");
            }

            @Override
            public Distance elevation() {
                return Distance.feet(3000);
            }
        };


        ConfigurationProvider configProvider = new FilesystemConfigurationProvider(Path.of(System.getProperty("user.home")).resolve(".overpoet"));
        EngineConfiguration config = new SimpleEngineConfiguration(location, configProvider);
        Engine engine = new Engine(config);

        engine.start();
        System.err.println( "start returned");
    }
}
