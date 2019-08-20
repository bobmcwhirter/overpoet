package com.overpoet.core.measurement;

public interface Distance {
    default double meters() {
        return feet() / 3.281d;
    }

    default double kilometers() {
        return meters() / 1000.0d;
    }

    default double feet() {
        return meters() * 3.281d;
    }

    default double miles() {
        return feet() / 5280.0d;
    }

    static Distance feet(final double feet) {
        return new Distance() {
            @Override
            public double feet() {
                return feet;
            }
        };
    }

    static Distance miles(final double miles) {
        return new Distance() {
            @Override
            public double feet() {
                return miles * 5280.0d;
            }
        };
    }

    static Distance meters(double meters) {
        return new Distance() {
            @Override
            public double meters() {
                return meters;
            }
        };
    }

    static Distance kilometers(double kilometers) {
        return new Distance() {
            @Override
            public double kilometers() {
                return kilometers;
            }
        };
    }

}
