package io.overpoet.automation.rule;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import io.overpoet.spi.Key;

class SensorNode<T> {

    SensorNode(Key key) {
        this.key = key;
    }

    private void addAlphaNode(AlphaNode<T> alphaNode) {
        this.alphaNodes.add( alphaNode );
    }

    AlphaNode<T> addAlphaNode(Function<T, Boolean> condition) {
        AlphaNode<T> alpha = new AlphaNode<>(condition);
        addAlphaNode(alpha);
        return alpha;
    }

    void assertValue(Agenda agenda, T value) {
        this.value = value;
        for (AlphaNode<T> each : alphaNodes) {
            each.assertValue(agenda, value);
        }
    }

    @Override
    public String toString() {
        return "[SensorNode: " + this.key + "]";
    }

    private final Key key;
    private T value;
    private Set<AlphaNode<T>> alphaNodes = new HashSet<>();
}
