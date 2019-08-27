package io.overpoet.netatmo.weather.temperature;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import io.overpoet.Key;
import io.overpoet.core.measurement.Temperature;
import io.overpoet.core.metadata.TemperatureMetadata;
import io.overpoet.core.sensor.Sensor;
import io.overpoet.core.sensor.TemperatureSensor;
import io.overpoet.json.AbstractJSONSensorLogic;
import io.overpoet.netatmo.weather.LogicRegistry;

public class OutsideTemperatureSensorLogic extends AbstractJSONSensorLogic<Temperature,Double> {

    private final static JsonPath PATH = JsonPath.compile("$.body.devices[0].modules[?(@.type == 'NAModule1')].dashboard_data.Temperature");

    public OutsideTemperatureSensorLogic(Converter<Temperature,Double> converter) {
        super(Double.class, converter, PATH);
    }

    public static boolean isApplicable(ReadContext document) {
        Object result = document.read(PATH);
        return result != null;
    }

    public static Sensor<?> of(Key key, LogicRegistry registry) {
        return new TemperatureSensor(key.append("temperature"),
                                     TemperatureMetadata.DEFAULT,
                                     registry.register(new OutsideTemperatureSensorLogic(Temperature::celsius)));
    }

}
