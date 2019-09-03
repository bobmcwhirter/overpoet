package io.overpoet.engine.rule;

public class Rule {

    public Rule(String id) {
        this.id = id;
    }

    public String id() {
        return this.id;
    }

    Rule when(Condition condition) {
        this.condition = condition;
        return this;
    }

    void build(RootNode root) {
        TokenPassingNode tail = this.condition.build(root);
        ActionNode actionNode = new ActionNode(this.action);
        tail.addInput(actionNode);
    }

    void then(Action action) {
        this.action = action;
    }

    private final String id;
    private Condition condition;
    private Action action;

}
