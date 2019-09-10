package io.overpoet.automation.rule;

import java.util.function.Function;

import io.overpoet.spi.Key;

public abstract class Condition {
    abstract TokenPassingNode build(RootNode root);

    public static <T> SensorCondition<T> sensor(Key key, Function<T, Boolean> expr) {
        return new SensorCondition<>(key, expr);
    }

    public static AndCondition and(Condition left, Condition right) {
        return new AndCondition(left, right);
    }

    public static OrCondition or(Condition left, Condition right) {
        return new OrCondition(left, right);
    }
}
