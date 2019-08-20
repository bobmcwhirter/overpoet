package com.overpoet.netatmo.weather.wind;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.overpoet.Key;
import com.overpoet.core.measurement.Speed;
import com.overpoet.core.metadata.SpeedMetadata;
import com.overpoet.core.sensor.Sensor;
import com.overpoet.core.sensor.SpeedSensor;
import com.overpoet.core.sensor.AbstractJSONSensorLogic;
import com.overpoet.netatmo.weather.LogicRegistry;

public class GustStrengthSensorLogic extends AbstractJSONSensorLogic<Speed,Integer> {

    private final static JsonPath PATH = JsonPath.compile("$.body.devices[0].modules[?(@.type == 'NAModule2')].dashboard_data.GustStrength");

    public GustStrengthSensorLogic(Converter<Speed,Integer> converter) {
        super(Integer.class, converter, PATH);
    }

    public static boolean isApplicable(ReadContext document) {
        Object result = document.read(PATH);
        System.err.println( "----> " + result);
        return result != null;
    }

    public static Sensor<?> of(Key key, Converter<Speed,Integer> converter, LogicRegistry registry) {
        return new SpeedSensor(key.append("gust-strength"), SpeedMetadata.DEFAULT.DEFAULT, registry.register(new GustStrengthSensorLogic(converter)));
    }

}
