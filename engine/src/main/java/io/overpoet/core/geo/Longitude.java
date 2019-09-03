package io.overpoet.core.geo;

public class Longitude extends Coordinate {

    Longitude(double decimalDegrees) {
        super(decimalDegrees);
    }

    public static Longitude west(double decimalDegrees) {
        return new Longitude(-Math.abs(decimalDegrees));
    }

    public static Longitude east(double decimalDegrees) {
        return new Longitude(Math.abs(decimalDegrees));
    }
}
