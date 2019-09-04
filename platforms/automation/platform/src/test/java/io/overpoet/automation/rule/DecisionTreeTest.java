package io.overpoet.automation.rule;

import java.util.concurrent.atomic.AtomicBoolean;

import io.overpoet.spi.metadata.IntegerMetadata;
import io.overpoet.spi.sensor.BaseSensorLogic;
import io.overpoet.spi.sensor.IntegerSensor;
import org.junit.Test;

import static io.overpoet.spi.Key.keyOf;
import static org.fest.assertions.api.Assertions.assertThat;


public class DecisionTreeTest {

    @Test
    public void testTree() {

        RootNode root = new RootNode();

        IntegerSensor sensor1 = new IntegerSensor(keyOf("sensor-1"), IntegerMetadata.DEFAULT, new BaseSensorLogic<>());
        IntegerSensor sensor2 = new IntegerSensor(keyOf("sensor-2"), IntegerMetadata.DEFAULT, new BaseSensorLogic<>());

        SensorNode<Integer> sensorNode1 = root.getSensorNode(sensor1);
        AlphaNode<Integer> greaterThan3 = sensorNode1.addAlphaNode((v -> v > 3));
        AlphaNode<Integer> lessThan10 = sensorNode1.addAlphaNode(v -> v < 10);

        JoinNode join = new JoinNode(JoinNode.Type.AND, greaterThan3, lessThan10);

        AtomicBoolean result = new AtomicBoolean(false);

        ActionNode action = new ActionNode(() -> {
            result.set(true);
        });

        join.addInput(action);

        root.assertSensor(sensor2, 7);
        assertThat(result.get()).isFalse();

        root.assertSensor(sensor1, 1);
        assertThat(result.get()).isFalse();

        root.assertSensor(sensor1, 22);
        assertThat(result.get()).isFalse();

        root.assertSensor(sensor1, 7);
        assertThat(result.get()).isTrue();

        result.set(false);

        root.assertSensor(sensor1, 7);
        assertThat(result.get()).isFalse();

        root.assertSensor(sensor1, 8);
        assertThat(result.get()).isFalse();

        root.assertSensor(sensor1, 1);
        assertThat(result.get()).isFalse();

        root.assertSensor(sensor1, 8);
        assertThat(result.get()).isTrue();


    }
}
