package com.overpoet;

import java.util.Arrays;

class KeyImpl implements Key {

    KeyImpl(String... parts) {
        this.parts = parts;
    }

    public Key append(String part) {
        String[] newParts = Arrays.copyOf(this.parts, this.parts.length + 1);
        newParts[this.parts.length] = part;
        return new KeyImpl(newParts);
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj instanceof KeyImpl ) {
            return Arrays.equals(this.parts, ((KeyImpl) obj).parts);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.parts);
    }

    @Override
    public String toString() {
        return String.join(".", this.parts);
    }

    private final String[] parts;

}
