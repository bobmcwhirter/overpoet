package com.overpoet.core.measurement;

public interface Bearing {

    Bearing MIN = of(0.0);
    Bearing MAX = of(360.0);

    double degrees();

    static Bearing of(double degrees) {
        if (degrees < 0.0d || degrees > 360.0d) {
            throw new IllegalArgumentException("Bearing must be between 0.0 and 360.0");
        }

        return new Bearing() {
            @Override
            public double degrees() {
                return degrees;
            }

            @Override
            public String toString() {
                return "[bearing: " + degrees + "°]";
            }
        };
    }
}
