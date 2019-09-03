package io.overpoet.engine.rule;

import java.util.function.Function;

import io.overpoet.spi.sensor.Sensor;

public abstract class Condition {
    abstract TokenPassingNode build(RootNode root);

    public static <T> SensorCondition<T> sensor(Sensor<T> sensor, Function<T, Boolean> expr) {
        return new SensorCondition<>(sensor, expr);
    }

    public static AndCondition and(Condition left, Condition right) {
        return new AndCondition(left, right);
    }

    public static OrCondition or(Condition left, Condition right) {
        return new OrCondition(left, right);
    }
}
