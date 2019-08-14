package com.overpoet;

public interface Key {

    Key append(String part);

    static Key of(String...parts) {
        return new KeyImpl(parts);
    }

    static Key keyOf(String...parts) {
        return of(parts);
    }
}
