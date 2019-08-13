package com.overpoet.core.rule;

class JoinNode extends AbstractNode {

    enum Type {
        AND,
        OR,
    }

    JoinNode() {
        this(Type.AND);
    }

    JoinNode(Type type) {
        this.type = type;
    }

    @Override
    boolean matched() {
        return this.matched;
    }

    private void checkMatch(Agenda agenda) {
        boolean newValue = this.matched;
        if ( this.type == Type.AND ) {
            newValue = this.leftValue && this.rightValue;
        } else if ( this.type == Type.OR ) {
            newValue = this.leftValue || this.rightValue;
        }
        if ( this.matched != newValue ) {
            this.matched = newValue;
            propagate(agenda);
        }
    }

    private void assertValueLeft(Agenda agenda, boolean value) {
        if ( this.leftValue != value ) {
            this.leftValue = value;
            checkMatch(agenda);
        }
    }

    private void assertValueRight(Agenda agenda, boolean value) {
        if ( this.rightValue != value ) {
            this.rightValue = value;
            checkMatch(agenda);
        }
    }

    TokenInput getLeftInput() {
        return this::assertValueLeft;
    }

    TokenInput getRightInput() {
        return this::assertValueRight;
    }

    private final Type type;

    private boolean matched = false;

    private boolean leftValue = false;
    private boolean rightValue = false;

}
