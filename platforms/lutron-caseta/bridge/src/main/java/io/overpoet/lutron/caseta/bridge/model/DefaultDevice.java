package io.overpoet.lutron.caseta.bridge.model;

import java.util.HashSet;
import java.util.Set;

public class DefaultDevice implements Device {

    public DefaultDevice(String href) {
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

    public void serialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String serialNumber() {
        return this.serialNumber;
    }

    public void modelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String modelNumber() {
        return this.modelNumber;
    }

    public void deviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String deviceType() {
        return this.deviceType;
    }

    public void zones(Set<Zone> zones) {
        if ( zones != null ) {
            this.zones = zones;
        } else {
            this.zones = new HashSet<>();
        }
    }

    public Set<Zone> zones() {
        return this.zones;
    }

    public void area(Area area) {
        this.area = area;
    }

    public Area area() {
        return this.area;
    }

    @Override
    public String toString() {
        return "[Device: " + this.href + "; type=" + this.deviceType + "; s#=" + this.serialNumber + "; m#=" + this.modelNumber + "; a=" + this.area + "; z=" + this.zones + "]";
    }

    private final String href;

    private String name;

    private String serialNumber;

    private String modelNumber;

    private String deviceType;

    private Set<Zone> zones;

    private Area area;
}
