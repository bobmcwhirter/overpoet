package com.overpoet.core.rule;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.overpoet.Key;
import com.overpoet.core.geo.Length;
import com.overpoet.core.geo.Location;
import com.overpoet.core.metadata.IntegerMetadata;
import com.overpoet.core.sensor.BaseSensorLogic;
import com.overpoet.core.sensor.IntegerSensor;
import com.overpoet.core.sensor.MockClock;
import com.overpoet.core.sensor.TimeMetadata;
import com.overpoet.core.sensor.TimeSensor;
import com.overpoet.core.geo.Point;
import org.junit.Test;

import static com.overpoet.Key.keyOf;
import static com.overpoet.core.geo.Latitude.north;
import static com.overpoet.core.geo.Longitude.west;
import static com.overpoet.core.rule.Condition.and;
import static com.overpoet.core.rule.Condition.sensor;
import static com.overpoet.core.rule.TimeCondition.beforeSunset;
import static org.fest.assertions.api.Assertions.assertThat;

public class RuleTest {

    @Test
    public void testSimple() throws Exception {

        IntegerSensor sensor1 = new IntegerSensor(keyOf("sensor-1"), IntegerMetadata.DEFAULT, new BaseSensorLogic<>());
        IntegerSensor sensor2 = new IntegerSensor(keyOf("sensor-2"), IntegerMetadata.DEFAULT, new BaseSensorLogic<>());

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

        RuleEngine engine = new RuleEngine();
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

        TimeSensor sensor = new TimeSensor(Key.keyOf("system.time"), TimeMetadata.INSTANCE, clock);

        Rule rule = new Rule("yo");

        AtomicBoolean result = new AtomicBoolean(false);

        rule.when(
                sensor(sensor, v -> v.includes(fourPM))
        ).then(() -> {
            result.set(true);
        });

        RuleEngine engine = new RuleEngine();
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
            public Length elevation() {
                return Length.feet(3000);
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

        TimeSensor sensor = new TimeSensor(Key.keyOf("system.time"), TimeMetadata.INSTANCE, clock);

        Rule rule = new Rule("yo");

        AtomicBoolean result = new AtomicBoolean(false);

        rule.when(
                beforeSunset(sensor, location, 30, ChronoUnit.MINUTES)
        ).then(() -> {
            result.set(true);
        });

        RuleEngine engine = new RuleEngine();
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
