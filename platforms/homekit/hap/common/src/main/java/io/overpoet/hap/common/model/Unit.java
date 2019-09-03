package io.overpoet.hap.common.model;


import io.overpoet.hap.common.model.impl.UnitImpl;

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
