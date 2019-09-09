package io.overpoet.lutron.leap.client.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class Universe {

    public synchronized Zone zone(String href) {
        //return this.zones.getOrDefault(href, new Zone(href));
        Zone zone = this.zones.get(href);
        if (zone == null) {
            zone = new Zone(href);
            this.zones.put(href, zone);
        }
        return zone;
    }

    public synchronized Area area(String href) {
        Area area = this.areas.get(href);
        if (area == null) {
            area = new Area(href);
            this.areas.put(href, area);
        }
        return area;
    }

    public synchronized Device device(String href) {
        Device device = this.devices.get(href);
        if (device == null) {
            device = new Device(href);
            this.devices.put(href, device);
        }
        return device;
    }

    public void onZoneAdded(Consumer<Zone> listener) {
        this.zoneAddedListener = listener;
    }

    //public void onZoneStatusUpdated(Consumer<ZoneStatus> listener) {
        //this.zoneStatusUpdatedListener = listener;
    //}

    public void onZoneAdded(Zone zone) {
        if ( this.zoneAddedListener != null ) {
            this.zoneAddedListener.accept(zone);
        }
    }

    //public void onZoneStatusUpdated(ZoneStatus status) {
        //if ( this.zoneStatusUpdatedListener != null ) {
            //this.zoneStatusUpdatedListener.accept(status);
        //}
    //}

    private final Map<String, Zone> zones = new HashMap<>();

    private final Map<String, Area> areas = new HashMap<>();

    private final Map<String, Device> devices = new HashMap<>();

    private Consumer<Zone> zoneAddedListener;

    private Consumer<ZoneStatus> zoneStatusUpdatedListener;
}
