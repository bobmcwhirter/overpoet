package com.overpoet.netatmo.weather;

import com.overpoet.core.sensor.AbstractJSONSensorLogic;

public interface LogicRegistry {
    <T extends AbstractJSONSensorLogic<?,?>> T register(T logic);
}
