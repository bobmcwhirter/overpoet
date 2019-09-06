package io.overpoet.lutron.leap.client.model;

public class ZoneStatus {

    public ZoneStatus(Zone zone) {
        this.zone = zone;
    }

    public Zone zone() {
        return this.zone;
    }

    public void switchedLevel(SwitchedLevel switchedLevel) {
        this.switchedLevel = switchedLevel;
    }

    public SwitchedLevel switchedLevel() {
        return this.switchedLevel;
    }

    public void level(Integer level) {
        this.level = level;
    }

    public Integer level() {
        return this.level;
    }

    @Override
    public String toString() {
        return "[ZoneStatus: " + zone + "; " + switchedLevel + "/" + level + "]";
    }

    private final Zone zone;
    private SwitchedLevel switchedLevel;
    private Integer level;
}
