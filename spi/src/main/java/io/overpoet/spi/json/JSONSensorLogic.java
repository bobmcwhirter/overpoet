package io.overpoet.spi.json;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import io.overpoet.spi.sensor.BaseSensorLogic;
import net.minidev.json.JSONArray;

public class JSONSensorLogic<T, JSONTYPE> extends BaseSensorLogic<T> {

    protected JSONSensorLogic(Class<JSONTYPE> inputType, Converter<T, JSONTYPE> converter, JsonPath path) {
        this.inputType = inputType;
        this.converter = converter;
        this.path = path;
    }

    protected T convert(JSONTYPE in) {
        if ( this.converter == null ) {
            return (T) in;
        }
        T out = this.converter.convert(in);
        return out;
    }

    public void process(ReadContext ctx) {
        JSONArray array = ctx.read(this.path);
        if ( array.isEmpty() ) {
            return;
        }
        JSONTYPE in = (JSONTYPE) array.get(0);
        T out = convert(in);
        sink( out );
    }


    private final Class<JSONTYPE> inputType;
    private final JsonPath path;
    private final Converter<T,JSONTYPE> converter;

    public interface Converter<V,JV> {
        V convert(JV value);
    }
}
