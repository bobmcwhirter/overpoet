package io.overpoet.lutron.caseta.bridge.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Universe {

    public Zone process(DefaultZone input) {
        return this.zones.merge(input.href(), input, (oldValue, newValue) -> {
            oldValue.controlType(newValue.controlType());
            oldValue.name(newValue.name());
            return oldValue;
        });
    }

    public Area process(DefaultArea input) {
        return this.areas.merge(input.href(), input, (oldValue, newValue) ->{
            oldValue.name(newValue.name());
            return oldValue;
        });
    }

    public Device process(DefaultDevice input) {
        return this.devices.merge(input.href(), input, (oldValue, newValue) ->{
            oldValue.name(newValue.name());
            oldValue.deviceType(newValue.deviceType());
            oldValue.modelNumber(newValue.modelNumber());
            oldValue.serialNumber(newValue.serialNumber());
            oldValue.zones(newValue.zones());
            oldValue.area(newValue.area());
            return oldValue;
        });
    }

    public Zone zone(String href) {
        return getZone(href);
    }

    public Device device(String href) {
        return getDevice(href);
    }

    public Area area(String href) {
        return getArea(href);
    }

    protected DefaultZone getZone(String href) {
        return this.zones.getOrDefault(href, new DefaultZone(href));
    }

    protected DefaultArea getArea(String href) {
        return this.areas.getOrDefault(href, new DefaultArea(href));
    }

    protected DefaultDevice getDevice(String href) {
        return this.devices.getOrDefault(href, new DefaultDevice(href));
    }

    private final Map<String, DefaultZone> zones = new ConcurrentHashMap<>();
    private final Map<String, DefaultArea> areas = new ConcurrentHashMap<>();
    private final Map<String, DefaultDevice> devices = new ConcurrentHashMap<>();

}
