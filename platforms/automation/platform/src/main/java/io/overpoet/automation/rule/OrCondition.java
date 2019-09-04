package io.overpoet.automation.rule;

public class OrCondition extends AbstractJoinCondition {

    public OrCondition(Condition left, Condition right) {
        super( JoinNode.Type.OR, left, right);
    }
}
