package io.overpoet.engine.rule;

public class AndCondition extends AbstractJoinCondition {

    public AndCondition(Condition left, Condition right) {
        super( JoinNode.Type.AND, left, right);
    }

}
