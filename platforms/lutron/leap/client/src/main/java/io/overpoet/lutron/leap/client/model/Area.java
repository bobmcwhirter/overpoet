package io.overpoet.lutron.leap.client.model;

public class Area {

    Area(String href) {
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

    @Override
    public String toString() {
        return "[Area: " + this.name + "]";
    }

    private String href;

    private String name;
}
