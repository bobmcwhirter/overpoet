package io.overpoet.spi.json;

import com.jayway.jsonpath.JsonPath;
import io.overpoet.spi.measurement.Speed;

public class SpeedSensorFactory extends JSONSensorFactory<Speed,Integer> {

    public SpeedSensorFactory(JSONSensor.Converter<Speed,Integer> converter, JsonPath path) {
        super(Integer.class, converter, path);
    }


}
