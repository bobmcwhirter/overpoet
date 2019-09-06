package io.overpoet.lutron.leap.client.model;

public enum ControlType {
    DIMMED,
    SWITCHED,
    UNKNOWN,
    ;

    public static ControlType of(String name) {
        if ( name.equalsIgnoreCase("dimmed")) {
            return DIMMED;
        }
        if ( name.equalsIgnoreCase( "switched")) {
            return SWITCHED;
        }

        return UNKNOWN;
    }
}
