package io.overpoet.spi.geo;

public class Coordinate {

    protected Coordinate(double decimalDegrees) {
        this.decimalDegrees = decimalDegrees;
    }

    public double decimalDegrees() {
        return this.decimalDegrees;
    }

    private final double decimalDegrees;
}
