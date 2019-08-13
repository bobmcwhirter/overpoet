package com.overpoet.core.rule;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.overpoet.Key;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.extractProperty;


public class DecisionTreeTest {

    @Test
    public void testTree() {

        RootNode root = new RootNode();

        SensorNode<Integer> sensor1 = root.getSensorNode(Key.of("sensor-1"));
        AlphaNode<Integer> greaterThan3 = new AlphaNode<>((v) -> v > 3);
        AlphaNode<Integer> lessThan10 = new AlphaNode<>((v) -> v < 10);

        sensor1.addAlphaNode(greaterThan3);
        sensor1.addAlphaNode(lessThan10);

        JoinNode join = new JoinNode();

        greaterThan3.addInput(join.getLeftInput());
        lessThan10.addInput(join.getRightInput());

        AtomicBoolean result = new AtomicBoolean(false);

        ActionNode action = new ActionNode(() -> {
            result.set(true);
        });

        join.addInput(action);

        root.assertSensor(Key.of("sensor-2"), 7);
        assertThat(result.get()).isFalse();

        root.assertSensor(Key.of("sensor-1"), 1);
        assertThat(result.get()).isFalse();

        root.assertSensor(Key.of("sensor-1"), 22);
        assertThat(result.get()).isFalse();

        root.assertSensor(Key.of("sensor-1"), 7);
        assertThat(result.get()).isTrue();

        result.set(false);

        root.assertSensor(Key.of("sensor-1"), 7);
        assertThat(result.get()).isFalse();

        root.assertSensor(Key.of("sensor-1"), 8);
        assertThat(result.get()).isFalse();

        root.assertSensor(Key.of("sensor-1"), 1);
        assertThat(result.get()).isFalse();

        root.assertSensor(Key.of("sensor-1"), 8);
        assertThat(result.get()).isTrue();


    }
}
