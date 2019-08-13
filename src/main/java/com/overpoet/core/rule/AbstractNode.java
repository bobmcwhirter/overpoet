package com.overpoet.core.rule;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractNode {

    void addInput(Input input) {
        this.inputs.add(input);
    }

    abstract boolean matched();

    void propagate() {
        boolean value = matched();
        for (Input input : inputs) {
            input.assertValue(value);
        }
    }

    private final Set<Input> inputs = new HashSet<>();
}
