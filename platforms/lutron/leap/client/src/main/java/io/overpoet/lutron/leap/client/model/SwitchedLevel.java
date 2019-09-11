package io.overpoet.lutron.leap.client.model;

public enum SwitchedLevel {
    ON("On"),
    OFF("Off"),
    ;

    SwitchedLevel(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return this.str;
    }

    public static SwitchedLevel of(String str) {
        if ( str.equalsIgnoreCase("On")) {
            return ON;
        }
        if ( str.equalsIgnoreCase("Off")) {
            return OFF;
        }

        return null;
    }

    private final String str;
}
