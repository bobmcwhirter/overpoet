package com.overpoet.core.rule;

import java.util.concurrent.atomic.AtomicBoolean;

import com.overpoet.core.metadata.IntegerMetadata;
import com.overpoet.core.sensor.BaseSensorLogic;
import com.overpoet.core.sensor.IntegerSensor;
import org.junit.Test;

import static com.overpoet.Key.keyOf;
import static com.overpoet.core.rule.Condition.and;
import static com.overpoet.core.rule.Condition.sensor;
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
                        sensor(sensor1, v->v > 3),
                        sensor(sensor1, v->v < 10)
                )
        ).then( ()->{
            result.set(true);
        });

        RuleEngine engine = new RuleEngine();
        engine.addRule( rule );

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
}
