package io.overpoet.spi.json;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

public class JSONSensorLogicFactory<T, JSONTYPE> {

    public JSONSensorLogicFactory(Class<JSONTYPE> inputType, JSONSensorLogic.Converter<T,JSONTYPE> converter, JsonPath path) {
        this.inputType = inputType;
        this.converter = converter;
        this.path = path;
    }

    public boolean isApplicable(ReadContext document) {
        Object result = document.read(this.path);
        return result != null;
    }

    public JSONSensorLogic<T, JSONTYPE> build() {
        return new JSONSensorLogic<T, JSONTYPE>(this.inputType, converter, this.path);
    }



    private final Class<JSONTYPE> inputType;
    private final JSONSensorLogic.Converter<T, JSONTYPE> converter;
    private final JsonPath path;
}
