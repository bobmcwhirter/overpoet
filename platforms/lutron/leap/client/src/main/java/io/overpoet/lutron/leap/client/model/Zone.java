package io.overpoet.lutron.leap.client.model;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Zone {

    Zone(String href) {
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

    public void device(Device device) {
        this.device = device;
    }

    public Device device() {
        return this.device;
    }

    public void addStatusChangeListener(Consumer<ZoneStatus> listener) {
        this.listeners.add(listener);
    }

    public void statusChanged(ZoneStatus status) {
        for (Consumer<ZoneStatus> listener : this.listeners) {
            listener.accept(status);
        }
    }

    public String toString() {
        return "[Zone: " + this.href + "; " + this.name + "; " + this.controlType + "; " + this.device + "]";
    }

    private final String href;

    private String name;

    private ControlType controlType;

    private Device device;

    private Set<Consumer<ZoneStatus>> listeners = new HashSet<>();
}
