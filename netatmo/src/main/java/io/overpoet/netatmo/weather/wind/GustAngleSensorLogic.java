package io.overpoet.netatmo.weather.wind;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import io.overpoet.Key;
import io.overpoet.core.measurement.Bearing;
import io.overpoet.core.metadata.BearingMetadata;
import io.overpoet.core.sensor.BearingSensor;
import io.overpoet.core.sensor.Sensor;
import io.overpoet.json.AbstractJSONSensorLogic;
import io.overpoet.netatmo.weather.LogicRegistry;

public class GustAngleSensorLogic extends AbstractJSONSensorLogic<Bearing,Integer> {

    private final static JsonPath PATH = JsonPath.compile("$.body.devices[0].modules[?(@.type == 'NAModule2')].dashboard_data.GustAngle");

    GustAngleSensorLogic() {
        super( Integer.class, Bearing::of, PATH );
    }

    public static boolean isApplicable(ReadContext document) {
        Object result = document.read(PATH);
        return result != null;
    }

    public static Sensor<?> of(Key key, LogicRegistry registry) {
        return new BearingSensor(key.append("gust-angle"),
                                 BearingMetadata.DEFAULT,
                                 registry.register(new GustAngleSensorLogic()));
    }

}
