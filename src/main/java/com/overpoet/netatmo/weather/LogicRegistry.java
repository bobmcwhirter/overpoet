package com.overpoet.netatmo.weather;

import com.overpoet.netatmo.weather.wind.BaseNetatamoSensorLogic;

public interface LogicRegistry {
    <T extends BaseNetatamoSensorLogic<?>> T register(T logic);
}
