package io.overpoet.automation.rule;

import java.util.function.Function;

import io.overpoet.spi.sensor.Sensor;

public class SensorCondition<T> extends Condition {

    public SensorCondition(Sensor<T> sensor, Function<T, Boolean> condition) {
        this.sensor = sensor;
        this.condition = condition;
    }

    @Override
    TokenPassingNode build(RootNode root) {
        SensorNode<T> sensorNode = root.getSensorNode(this.sensor);
        AlphaNode<T> alphaNode = sensorNode.addAlphaNode(this.condition);
        return alphaNode;
    }

    private final Sensor<T> sensor;
    private final Function<T, Boolean> condition;
}
