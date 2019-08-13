package com.overpoet.core.rule;

public class ActionNode implements Input {

    ActionNode(Action action) {
        this.action = action;
    }

    @Override
    public void assertValue(boolean value) {
        if ( value ) {
            try {
                action.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private final Action action;
}
