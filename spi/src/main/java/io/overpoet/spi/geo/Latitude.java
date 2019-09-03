package io.overpoet.spi.geo;

public class Latitude extends Coordinate {

    Latitude(double decimalDegrees) {
        super(decimalDegrees);
    }

    public static Latitude north(double decimalDegrees) {
        return new Latitude(Math.abs(decimalDegrees));
    }

    public static Latitude south(double decimalDegrees) {
        return new Latitude(-Math.abs(decimalDegrees));
    }
}
