package io.overpoet.lutron.leap.client.model;

public enum SwitchedLevel {
    ON,
    OFF,
    ;

    public static SwitchedLevel of(String str) {
        if ( str.equalsIgnoreCase("On")) {
            return ON;
        }
        if ( str.equalsIgnoreCase("Off")) {
            return OFF;
        }

        return null;
    }
}
