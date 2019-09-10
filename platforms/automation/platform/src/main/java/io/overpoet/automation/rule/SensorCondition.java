package io.overpoet.automation.rule;

import java.util.function.Function;

import io.overpoet.spi.TypedKey;

public class SensorCondition<T> extends Condition {

    public SensorCondition(TypedKey<T> key, Function<T, Boolean> condition) {
        this.key = key;
        this.condition = condition;
    }

    @Override
    TokenPassingNode build(RootNode root) {
        SensorNode<T> sensorNode = root.getSensorNode(this.key);
        AlphaNode<T> alphaNode = sensorNode.addAlphaNode(this.condition);
        return alphaNode;
    }


    private final TypedKey<T> key;
    private final Function<T, Boolean> condition;
}
