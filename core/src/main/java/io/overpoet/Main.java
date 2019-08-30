package io.overpoet;

import java.nio.file.Path;
import java.time.ZoneId;

import io.overpoet.core.engine.ConfigurationProvider;
import io.overpoet.core.engine.Engine;
import io.overpoet.core.engine.EngineConfiguration;
import io.overpoet.core.engine.FilesystemConfigurationProvider;
import io.overpoet.core.engine.SimpleEngineConfiguration;
import io.overpoet.core.measurement.Distance;
import io.overpoet.core.geo.Location;
import io.overpoet.core.geo.Point;
import io.overpoet.core.geo.Latitude;
import io.overpoet.core.geo.Longitude;

import static java.time.ZoneId.of;

public class Main {

    public static void main(String...args) {
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
