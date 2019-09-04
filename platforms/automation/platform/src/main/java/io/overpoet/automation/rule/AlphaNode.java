package io.overpoet.automation.rule;

import java.util.function.Function;

class AlphaNode<T> extends TokenPassingNode {

    AlphaNode(Function<T,Boolean> condition) {
        this.condition = condition;
    }

    @Override
    boolean matched() {
        return this.matched;
    }

    void assertValue(Agenda agenda, T value) {
        if ( this.condition.apply(value)) {
            if ( ! this.matched ) {
                this.matched = true;
                propagate(agenda);
            }
        } else {
            if ( this.matched ) {
                this.matched = false;
                propagate(agenda);
            }
        }
    }

    private final Function<T, Boolean> condition;
    private boolean matched;
}
