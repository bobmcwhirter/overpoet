package com.overpoet.core.geo;

public interface Length {
    double meters();
    double kilometers();

    double feet();
    double miles();

    static Length feet(final long feet) {
        return new Length() {
            @Override
            public double meters() {
                return feet / 3.281d;
            }

            @Override
            public double kilometers() {
                return meters() / 1000.0d;
            }

            @Override
            public double feet() {
                return feet;
            }

            @Override
            public double miles() {
                return feet / 5280.0d;
            }
        };
    }

    static Length meters(long meters) {
        return new Length() {
            @Override
            public double meters() {
                return meters;
            }

            @Override
            public double kilometers() {
                return meters / 1000.0d;
            }

            @Override
            public double feet() {
                return meters * 3.281d;
            }

            @Override
            public double miles() {
                return meters / 1609.344d;
            }
        };
    }
}
