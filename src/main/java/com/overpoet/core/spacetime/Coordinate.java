package com.overpoet.core.spacetime;

public class Coordinate {

    protected Coordinate(double decimalDegrees) {
        this.decimalDegrees = decimalDegrees;
    }

    double decimalDegrees() {
        return this.decimalDegrees;
    }

    private final double decimalDegrees;
}
