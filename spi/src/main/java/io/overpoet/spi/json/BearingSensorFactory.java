package io.overpoet.spi.json;

import com.jayway.jsonpath.JsonPath;
import io.overpoet.spi.measurement.Bearing;

public class BearingSensorFactory extends JSONSensorFactory<Bearing,Integer> {

    BearingSensorFactory(JsonPath path) {
        super( Integer.class, Bearing::of, path );
    }

}
