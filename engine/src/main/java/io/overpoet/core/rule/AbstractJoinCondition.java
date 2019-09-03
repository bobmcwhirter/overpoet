package io.overpoet.core.rule;

public abstract class AbstractJoinCondition extends Condition {
    public AbstractJoinCondition(JoinNode.Type op, Condition left, Condition right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    TokenPassingNode build(RootNode root) {
        TokenPassingNode builtLeft = this.left.build(root);
        TokenPassingNode builtRight = this.right.build(root);
        return new JoinNode(this.op, builtLeft, builtRight);
    }

    private final JoinNode.Type op;
    private final Condition left;
    private final Condition right;
}
