package com.overpoet.netatmo.weather;

import com.overpoet.json.AbstractJSONSensorLogic;

public interface LogicRegistry {
    <T extends AbstractJSONSensorLogic<?,?>> T register(T logic);
}
