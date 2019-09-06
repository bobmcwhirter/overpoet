package io.overpoet.lutron.caseta.bridge.model;

public class DefaultArea implements Area {

    public DefaultArea(String href) {
        this.href = href;
    }

    @Override
    public String href() {
        return this.href;
    }

    public void name(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String toString() {
        return "[Area: " + this.name + "]";
    }

    private String href;

    private String name;
}
