package com.overpoet.core.rule;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractNode {

    void addInput(TokenInput input) {
        this.inputs.add(input);
    }

    abstract boolean matched();

    void propagate(Agenda agenda) {
        boolean value = matched();
        for (TokenInput input : inputs) {
            input.assertToken(agenda, value);
        }
    }

    private final Set<TokenInput> inputs = new HashSet<>();
}
