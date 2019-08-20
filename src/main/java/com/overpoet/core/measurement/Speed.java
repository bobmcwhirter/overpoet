package com.overpoet.core.measurement;

import java.time.Duration;

import static com.overpoet.core.measurement.Distance.kilometers;
import static com.overpoet.core.measurement.Distance.miles;
import static java.time.temporal.ChronoUnit.HOURS;

public interface Speed {
    Distance distance();
    Duration duration();

    default double milesPerHour() {
        return distance().miles() / (duration().toMinutes() / 60.0d);
    }

    default double knots() {
        return ( distance().miles() / ( duration().toMinutes() / 60.0d) ) / 1.151d;
    }

    default double kilometersPerHour() {
        return ( distance().kilometers() ) / ( duration().toMinutes() / 60.d);
    }

    static Speed of(Distance distance, Duration duration) {
        return new Speed() {
            @Override
            public Distance distance() {
                return distance;
            }

            @Override
            public Duration duration() {
                return duration;
            }

            @Override
            public String toString() {
                return "[speed: " + milesPerHour() + "mph; " + kilometersPerHour() + "kph; " + knots() + "knots]";
            }
        };
    }

    static Speed milesPerHour(double mph) {
        return of(miles(mph), Duration.of(1, HOURS));
    }

    static Speed knots(double knots) {
        return of(miles(knots*1.151d), Duration.of(1, HOURS));
    }

    static Speed kilometersPerHour(double kph) {
        return of(kilometers(kph), Duration.of(1, HOURS));
    }
}
