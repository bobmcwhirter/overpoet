package io.overpoet.json;

import com.jayway.jsonpath.JsonPath;
import io.overpoet.core.measurement.Speed;

public class SpeedSensorLogicFactory extends JSONSensorLogicFactory<Speed,Integer> {

    public SpeedSensorLogicFactory(JSONSensorLogic.Converter<Speed,Integer> converter, JsonPath path) {
        super(Integer.class, converter, path);
    }


}
