package io.overpoet.automation.rule;

import java.util.concurrent.atomic.AtomicBoolean;

import io.overpoet.spi.TypedKey;
import org.junit.Test;

import static io.overpoet.spi.Key.keyOf;
import static org.fest.assertions.api.Assertions.assertThat;


public class DecisionTreeTest {

    @Test
    public void testTree() {

        RootNode root = new RootNode();

        TypedKey<Integer> key1 = new TypedKey<>(keyOf("sensor-1"), Integer.class);
        TypedKey<Integer> key2 = new TypedKey<>(keyOf("sensor-2"), Integer.class);

        SensorNode<Integer> sensorNode1 = root.getSensorNode(key1);
        AlphaNode<Integer> greaterThan3 = sensorNode1.addAlphaNode((v -> v > 3));
        AlphaNode<Integer> lessThan10 = sensorNode1.addAlphaNode(v -> v < 10);

        JoinNode join = new JoinNode(JoinNode.Type.AND, greaterThan3, lessThan10);

        AtomicBoolean result = new AtomicBoolean(false);

        ActionNode action = new ActionNode(() -> {
            result.set(true);
        });

        join.addInput(action);

        root.assertSensor(key2, 7);
        assertThat(result.get()).isFalse();

        root.assertSensor(key1, 1);
        assertThat(result.get()).isFalse();

        root.assertSensor(key2, 22);
        assertThat(result.get()).isFalse();

        root.assertSensor(key1, 7);
        assertThat(result.get()).isTrue();

        result.set(false);

        root.assertSensor(key1, 7);
        assertThat(result.get()).isFalse();

        root.assertSensor(key1, 8);
        assertThat(result.get()).isFalse();

        root.assertSensor(key1, 1);
        assertThat(result.get()).isFalse();

        root.assertSensor(key1, 8);
        assertThat(result.get()).isTrue();
    }
}
