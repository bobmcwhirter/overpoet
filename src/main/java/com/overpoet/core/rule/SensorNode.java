package com.overpoet.core.rule;

import java.util.HashSet;
import java.util.Set;

import com.overpoet.Key;

class SensorNode<T> {

    SensorNode(Key key) {

    }

    void addAlphaNode(AlphaNode<T> alphaNode) {
        this.alphaNodes.add( alphaNode );
        if ( this.value != null ) {
            alphaNode.assertValue(this.value);
        }
    }

    void assertValue(T value) {
        this.value = value;
        for (AlphaNode<T> each : alphaNodes) {
            each.assertValue(value);
        }
    }

    private T value;
    private Set<AlphaNode<T>> alphaNodes = new HashSet<>();
}
