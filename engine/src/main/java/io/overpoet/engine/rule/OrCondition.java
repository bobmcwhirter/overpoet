package io.overpoet.engine.rule;

public class OrCondition extends AbstractJoinCondition {

    public OrCondition(Condition left, Condition right) {
        super( JoinNode.Type.OR, left, right);
    }
}
