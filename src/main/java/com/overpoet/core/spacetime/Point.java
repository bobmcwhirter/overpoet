package com.overpoet.core.spacetime;

public class Point {

    public Point(Latitude latitude, Longitude longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Longitude longitude() {
        return longitude;
    }

    public Latitude latitude() {
        return this.latitude;
    }

    private final Latitude latitude;
    private final Longitude longitude;
}
