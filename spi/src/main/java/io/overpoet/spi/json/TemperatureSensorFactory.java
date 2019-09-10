package io.overpoet.spi.json;

import com.jayway.jsonpath.JsonPath;
import io.overpoet.spi.measurement.Temperature;

public class TemperatureSensorFactory extends JSONSensorFactory<Temperature,Double> {

    //private final static JsonPath PATH = JsonPath.compile("$.body.devices[0].modules[?(@.type == 'NAModule1')].dashboard_data.Temperature");

    public TemperatureSensorFactory(JsonPath path) {
        super(Double.class, Temperature::celsius, path);
    }


    /*
    public static Sensor<?> of(Key key, LogicRegistry registry) {
        return new TemperatureSensor(key.append("temperature"),
                                     TemperatureMetadata.DEFAULT,
                                     registry.register(new TemperatureSensorLogicFactory()));
    }
     */

}
