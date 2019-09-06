package io.overpoet.lutron.caseta.bridge.model;

public class DefaultZone implements Zone {

    public DefaultZone(String href) {
        this.href = href;
    }

    public String href() {
        return this.href;
    }

    public void name(String name) {
        this.name = name;
    }

    public String name() {
        return this.name;
    }

    public void controlType(ControlType controlType) {
        this.controlType = controlType;
    }

    public ControlType controlType() {
        return this.controlType;
    }

    public String toString() {
        return "[Zone: " + this.href + "; " + this.name + "; " + this.controlType + "]";
    }

    private final String href;

    private String name;

    private ControlType controlType;
}
