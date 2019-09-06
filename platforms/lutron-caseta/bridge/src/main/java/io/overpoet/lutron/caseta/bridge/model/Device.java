package io.overpoet.lutron.caseta.bridge.model;

import java.util.Set;

public interface Device {

    String href();

    String name();

    String serialNumber();

    String modelNumber();

    String deviceType();

    Area area();

    Set<Zone> zones();
}
