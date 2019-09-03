package io.overpoet.spi.json;

import com.jayway.jsonpath.JsonPath;
import io.overpoet.spi.measurement.Bearing;

public class BearingSensorLogicFactory extends JSONSensorLogicFactory<Bearing,Integer> {

    BearingSensorLogicFactory(JsonPath path) {
        super( Integer.class, Bearing::of, path );
    }

}
