package com.overpoet;

import java.nio.file.Path;
import java.time.ZoneId;

import com.overpoet.core.engine.ConfigurationProvider;
import com.overpoet.core.engine.Engine;
import com.overpoet.core.engine.EngineConfiguration;
import com.overpoet.core.engine.FilesystemConfigurationProvider;
import com.overpoet.core.engine.SimpleEngineConfiguration;
import com.overpoet.core.measurement.Distance;
import com.overpoet.core.geo.Location;
import com.overpoet.core.geo.Point;

import static com.overpoet.core.geo.Latitude.north;
import static com.overpoet.core.measurement.Distance.feet;
import static com.overpoet.core.geo.Longitude.west;
import static java.time.ZoneId.of;

public class Main {

    public static void main(String...args) {
        Location location = new Location() {
            @Override
            public Point point() {
                return new Point(north(36.57), west(81.05));
            }

            @Override
            public ZoneId getZoneId() {
                return of("America/New_York");
            }

            @Override
            public Distance elevation() {
                return feet(3000);
            }
        };

        ConfigurationProvider configProvider = new FilesystemConfigurationProvider(Path.of(System.getProperty("user.home")).resolve( ".overpoet"));
        EngineConfiguration config = new SimpleEngineConfiguration(location, configProvider);
        Engine engine = new Engine(config);

        engine.start();
    }
}
