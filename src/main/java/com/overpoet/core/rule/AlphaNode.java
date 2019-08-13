package com.overpoet.core.rule;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

class AlphaNode<T> extends AbstractNode {

    AlphaNode(Function<T,Boolean> condition) {
        this.condition = condition;
    }

    void addInput(Input input) {
        this.inputs.add( input );
    }

    @Override
    boolean matched() {
        return this.matched;
    }

    void assertValue(T value) {
        if ( this.condition.apply(value)) {
            this.matched = true;
        } else {
            this.matched = false;
        }

        propagate();
    }

    private final Function<T, Boolean> condition;
    private final Set<Input> inputs = new HashSet<>();
    private boolean matched;
}
