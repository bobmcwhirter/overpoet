package com.overpoet.core.rule;

import java.util.HashSet;
import java.util.Set;

import com.overpoet.Key;

class SensorNode<T> {

    SensorNode(Key key) {
        this.key = key;
    }

    void addAlphaNode(AlphaNode<T> alphaNode) {
        this.alphaNodes.add( alphaNode );
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
