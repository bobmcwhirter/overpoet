package io.overpoet.spi;

public interface Key {

    Key append(String part);
    Key append(String...parts);

    static Key of(String...parts) {
        return new KeyImpl(parts);
    }

    static Key keyOf(String...parts) {
        return of(parts);
    }
}
