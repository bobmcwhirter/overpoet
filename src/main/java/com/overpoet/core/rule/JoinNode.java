package com.overpoet.core.rule;

class JoinNode extends AbstractNode {

    enum Type {
        AND,
        OR,
    }

    JoinNode(Type type) {
        this.type = type;
    }

    @Override
    boolean matched() {
        return false;
    }

    void checkMatch() {
        boolean newValue = this.matched;
        if ( this.type == Type.AND ) {
            newValue = this.leftValue && this.rightValue;
        } else if ( this.type == Type.OR ) {
            newValue = this.leftValue || this.rightValue;
        }
        if ( this.matched != newValue ) {
            this.matched = newValue;
            propagate();
        }
    }

    void assertValueLeft(boolean value) {
        if ( this.leftValue != value ) {
            this.leftValue = value;
            checkMatch();
        }
    }

    void assertValueRight(boolean value) {
        if ( this.rightValue != value ) {
            this.rightValue = value;
            checkMatch();
        }
    }

    Input getLeftInput() {
        return value -> assertValueLeft(value);
    }

    Input getRightInput() {
        return value -> assertValueRight(value);
    }

    private final Type type;

    private boolean matched = false;

    private boolean leftValue = false;
    private boolean rightValue = false;

}
