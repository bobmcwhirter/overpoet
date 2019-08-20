package com.overpoet.hap.client.model;

import com.overpoet.hap.client.model.impl.UnitImpl;

/**
 * Created by bob on 9/14/18.
 */
public interface Unit {

    Unit CELSIUS = new UnitImpl("celsius");
    Unit PERCENTAGE = new UnitImpl("percentage");
    Unit ARCDEGREES = new UnitImpl("arcdegrees");
    Unit LUX = new UnitImpl("lux");
    Unit SECONDS = new UnitImpl("seconds");

    String getName();
}
