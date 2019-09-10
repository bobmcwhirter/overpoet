package io.overpoet.automation.rule;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.overpoet.spi.TypedKey;
import io.overpoet.spi.chrono.TimeSlice;
import io.overpoet.spi.geo.Location;
import io.overpoet.spi.geo.Point;
import io.overpoet.spi.measurement.Distance;
import io.overpoet.spi.sensor.MockClock;
import org.junit.Test;

import static io.overpoet.automation.rule.Condition.and;
import static io.overpoet.automation.rule.Condition.sensor;
import static io.overpoet.automation.rule.TimeCondition.beforeSunset;
import static io.overpoet.spi.Key.keyOf;
import static io.overpoet.spi.geo.Latitude.north;
import static io.overpoet.spi.geo.Longitude.west;
import static org.fest.assertions.api.Assertions.assertThat;

public class RuleTest {

    @Test
    public void testSimple() throws Exception {

        TypedKey<Integer> sensor1 = new TypedKey<>(keyOf("sensor-1"), Integer.class);
        TypedKey<Integer> sensor2 = new TypedKey<>(keyOf("sensor-2"), Integer.class);

        Rule rule = new Rule("yo");

        AtomicBoolean result = new AtomicBoolean(false);

        rule.when(
                and(
                        sensor(sensor1, v -> v > 3),
                        sensor(sensor1, v -> v < 10)
                )
        ).then(() -> {
            result.set(true);
        });

        RuleEngine engine = new RuleEngine(null);
        engine.addRule(rule);

        engine.assertSensor(sensor2, 7);
        assertThat(result.get()).isFalse();

        engine.assertSensor(sensor1, 1);
        assertThat(result.get()).isFalse();

        engine.assertSensor(sensor1, 22);
        assertThat(result.get()).isFalse();

        engine.assertSensor(sensor1, 7);
        assertThat(result.get()).isTrue();

        result.set(false);

        engine.assertSensor(sensor1, 7);
        assertThat(result.get()).isFalse();

        engine.assertSensor(sensor1, 8);
        assertThat(result.get()).isFalse();

        engine.assertSensor(sensor1, 1);
        assertThat(result.get()).isFalse();

        engine.assertSensor(sensor1, 8);
        assertThat(result.get()).isTrue();
    }

    @Test
    public void testTime() {

        MockClock clock = new MockClock(ZonedDateTime.of(
                2019,
                8,
                14,
                15,
                40,
                00,
                00,
                ZoneId.of("America/New_York")));

        LocalTime fourPM = LocalTime.of(
                16,
                00,
                00);

        TypedKey<TimeSlice> sensor = new TypedKey<>(keyOf("system.time"), TimeSlice.class);

        Rule rule = new Rule("yo");

        AtomicBoolean result = new AtomicBoolean(false);

        rule.when(
                sensor(sensor, v -> v.includes(fourPM))
        ).then(() -> {
            result.set(true);
        });

        RuleEngine engine = new RuleEngine(null);
        engine.addRule(rule);

        engine.assertSensor(sensor, clock.current());
        assertThat(result.get()).isFalse();

        clock.tick(Duration.of(19, ChronoUnit.MINUTES));
        engine.assertSensor(sensor, clock.current());
        assertThat(result.get()).isFalse();

        clock.tick(Duration.of(59, ChronoUnit.SECONDS));
        engine.assertSensor(sensor, clock.current());
        assertThat(result.get()).isFalse();

        clock.tick(Duration.of(4, ChronoUnit.MINUTES));
        engine.assertSensor(sensor, clock.current());
        assertThat(result.get()).isTrue();

        result.set(false);

        clock.tick(Duration.of(4, ChronoUnit.SECONDS));
        engine.assertSensor(sensor, clock.current());
        assertThat(result.get()).isFalse();

    }

    @Test
    public void testSunset() {

        Location location = new Location() {
            @Override
            public Point point() {
                return new Point(north(36.9485), west(81.0848));
            }

            @Override
            public ZoneId getZoneId() {
                return ZoneId.of("America/New_York");
            }

            @Override
            public Distance elevation() {
                return Distance.feet(3000);
            }
        };

        MockClock clock = new MockClock(ZonedDateTime.of(
                2019,
                8,
                14,
                19,
                30,
                00,
                00,
                ZoneId.of("America/New_York")
        ));

        //TimeSensor sensor = new TimeSensor(Key.keyOf("system.time"), TimeMetadata.INSTANCE, clock);
        TypedKey<TimeSlice> sensor = new TypedKey<>(keyOf("system.time"), TimeSlice.class );

        Rule rule = new Rule("yo");

        AtomicBoolean result = new AtomicBoolean(false);

        rule.when(
                beforeSunset(sensor, location, 30, ChronoUnit.MINUTES)
        ).then(() -> {
            result.set(true);
        });

        RuleEngine engine = new RuleEngine(null);
        engine.addRule(rule);

        engine.assertSensor(sensor, clock.current());
        assertThat(result.get()).isFalse();

        clock.tick(Duration.of(5, ChronoUnit.MINUTES));
        engine.assertSensor(sensor, clock.current());
        assertThat(result.get()).isFalse();

        clock.tick(Duration.of(5, ChronoUnit.MINUTES));
        engine.assertSensor(sensor, clock.current());
        assertThat(result.get()).isFalse();

        clock.tick(Duration.of(5, ChronoUnit.MINUTES));
        engine.assertSensor(sensor, clock.current());
        assertThat(result.get()).isFalse();

        // 7:50pm, 30 prior to actual sunset
        clock.tick(Duration.of(5, ChronoUnit.MINUTES));
        engine.assertSensor(sensor, clock.current());
        assertThat(result.get()).isTrue();

        result.set(false);

        clock.tick(Duration.of(5, ChronoUnit.MINUTES));
        engine.assertSensor(sensor, clock.current());
        assertThat(result.get()).isFalse();

        clock.tick(Duration.of(5, ChronoUnit.MINUTES));
        engine.assertSensor(sensor, clock.current());
        assertThat(result.get()).isFalse();

        clock.tick(Duration.of(5, ChronoUnit.MINUTES));
        engine.assertSensor(sensor, clock.current());
        assertThat(result.get()).isFalse();

        clock.tick(Duration.of(5, ChronoUnit.MINUTES));
        engine.assertSensor(sensor, clock.current());
        assertThat(result.get()).isFalse();


    }
}
