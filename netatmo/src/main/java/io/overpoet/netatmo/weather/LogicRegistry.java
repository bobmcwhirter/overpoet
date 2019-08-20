package io.overpoet.netatmo.weather;

import io.overpoet.json.AbstractJSONSensorLogic;

public interface LogicRegistry {
    <T extends AbstractJSONSensorLogic<?,?>> T register(T logic);
}
