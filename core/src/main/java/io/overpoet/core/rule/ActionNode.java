package io.overpoet.core.rule;

public class ActionNode implements TokenInput {

    ActionNode(Action action) {
        this.action = action;
    }

    @Override
    public void assertToken(Agenda agenda, boolean value) {
        if (value) {
            agenda.addAction(this.action);
        } else {
            agenda.removeAction(this.action);
        }
    }

    private final Action action;
}
