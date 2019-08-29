package io.overpoet.json;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import io.overpoet.core.sensor.BaseSensorLogic;
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
        System.err.println( "processing: " + ctx );
        JSONArray array = ctx.read(this.path);
        System.err.println( "result: " + array );
        if ( array.isEmpty() ) {
            return;
        }
        JSONTYPE in = (JSONTYPE) array.get(0);
        System.err.println( "in: " + in );
        T out = convert(in);
        System.err.println( "out: " + out );
        sink( out );
    }


    private final Class<JSONTYPE> inputType;
    private final JsonPath path;
    private final Converter<T,JSONTYPE> converter;

    public interface Converter<V,JV> {
        V convert(JV value);
    }
}
