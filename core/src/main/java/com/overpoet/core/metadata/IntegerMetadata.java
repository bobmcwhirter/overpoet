package com.overpoet.core.metadata;

public class IntegerMetadata implements Metadata<Integer> {

    public static final IntegerMetadata DEFAULT = new IntegerMetadata(Integer.MIN_VALUE, Integer.MAX_VALUE);

    public IntegerMetadata(int minimum, int maximum) {
        this.minimum = minimum;
        this.maximum = maximum;

    }

    public int minimum() {
        return this.minimum;
    }

    public int maximum() {
        return this.maximum;
    }

    private final int minimum;
    private final int maximum;
}
