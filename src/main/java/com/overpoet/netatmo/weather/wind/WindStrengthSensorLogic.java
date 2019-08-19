package com.overpoet.netatmo.weather.wind;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.overpoet.Key;
import com.overpoet.core.metadata.IntegerMetadata;
import com.overpoet.core.sensor.IntegerSensor;
import com.overpoet.core.sensor.Sensor;
import com.overpoet.netatmo.weather.LogicRegistry;

public class WindStrengthSensorLogic extends BaseNetatamoSensorLogic<Integer> {

    private final static JsonPath PATH = JsonPath.compile("$.body.devices[0].modules[?(@.type == 'NAModule2')].dashboard_data.WindStrength");

    public WindStrengthSensorLogic() {
        super(PATH);
    }

    public static boolean isApplicable(ReadContext document) {
        Object result = document.read(PATH);
        System.err.println( "----> " + result);
        return result != null;
    }

    public static Sensor<?> of(Key key, LogicRegistry registry) {
        return new IntegerSensor(key, IntegerMetadata.DEFAULT, registry.register(new WindStrengthSensorLogic()));
    }

}
