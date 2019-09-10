package io.overpoet.automation.rule;

import java.util.function.Function;

import io.overpoet.spi.Key;

public class SensorCondition<T> extends Condition {

    public SensorCondition(Key key, Function<T, Boolean> condition) {
        this.key = key;
        this.condition = condition;
    }

    @Override
    TokenPassingNode build(RootNode root) {
        SensorNode<T> sensorNode = root.getSensorNode(this.key);
        AlphaNode<T> alphaNode = sensorNode.addAlphaNode(this.condition);
        return alphaNode;
    }


    private final Key key;
    private final Function<T, Boolean> condition;
}
