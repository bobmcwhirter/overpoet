package io.overpoet.json;

import com.jayway.jsonpath.JsonPath;
import io.overpoet.core.measurement.Bearing;

public class BearingSensorLogicFactory extends JSONSensorLogicFactory<Bearing,Integer> {

    BearingSensorLogicFactory(JsonPath path) {
        super( Integer.class, Bearing::of, path );
    }

}
