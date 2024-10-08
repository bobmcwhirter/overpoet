package io.overpoet.spi.measurement;

public interface Temperature {

    default double celsius() {
        return (fahrenheit() - 32.0d) * 5.0d/9.0d;
    }

    default double fahrenheit() {
        return (celsius() * (9.0d/5.0d)) + 32.0d;

    }
    default double kelvin() {
        return celsius() + 273.15d;
    }

    static Temperature fahrenheit(double f) {
        return new Temperature() {
            @Override
            public double fahrenheit() {
                return f;
            }

            @Override
            public String toString() {
                return "[temperature: " + f + "f]";
            }
        };
    }

    static Temperature celsius(double c) {
        return new Temperature() {
            @Override
            public double celsius() {
                return c;
            }

            @Override
            public String toString() {
                return "[temperature: " + c + "c]";
            }
        };
    }

    static Temperature kelvin(double k) {
        return new Temperature() {
            @Override
            public double kelvin() {
                return k;
            }

            @Override
            public String toString() {
                return "[temperature: " + k + "k]";
            }
        };
    }
}
