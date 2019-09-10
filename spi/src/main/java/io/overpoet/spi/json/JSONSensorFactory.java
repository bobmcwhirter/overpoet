package io.overpoet.spi.json;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

public class JSONSensorFactory<T, JSONTYPE> {

    public JSONSensorFactory(Class<JSONTYPE> inputType, JSONSensor.Converter<T,JSONTYPE> converter, JsonPath path) {
        this.inputType = inputType;
        this.converter = converter;
        this.path = path;
    }

    public boolean isApplicable(ReadContext document) {
        Object result = document.read(this.path);
        return result != null;
    }

    public JSONSensor<T, JSONTYPE> build() {
        return new JSONSensor<>(this.inputType, converter, this.path);
    }



    private final Class<JSONTYPE> inputType;
    private final JSONSensor.Converter<T, JSONTYPE> converter;
    private final JsonPath path;
}
