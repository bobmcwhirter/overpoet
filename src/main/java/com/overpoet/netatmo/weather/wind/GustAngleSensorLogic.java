package com.overpoet.netatmo.weather.wind;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.overpoet.Key;
import com.overpoet.core.measurement.Bearing;
import com.overpoet.core.metadata.BearingMetadata;
import com.overpoet.core.sensor.BearingSensor;
import com.overpoet.core.sensor.Sensor;
import com.overpoet.core.sensor.AbstractJSONSensorLogic;
import com.overpoet.netatmo.weather.LogicRegistry;

public class GustAngleSensorLogic extends AbstractJSONSensorLogic<Bearing,Integer> {

    private final static JsonPath PATH = JsonPath.compile("$.body.devices[0].modules[?(@.type == 'NAModule2')].dashboard_data.GustAngle");

    GustAngleSensorLogic() {
        super( Integer.class, Bearing::of, PATH );
    }

    public static boolean isApplicable(ReadContext document) {
        Object result = document.read(PATH);
        System.err.println( "----> " + result);
        return result != null;
    }

    public static Sensor<?> of(Key key, LogicRegistry registry) {
        return new BearingSensor(key.append("gust-strength"), BearingMetadata.DEFAULT, registry.register(new GustAngleSensorLogic()));
    }

}
