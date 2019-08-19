package com.overpoet.netatmo.weather.wind;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.overpoet.core.sensor.BaseSensorLogic;

public abstract class BaseNetatamoSensorLogic<T> extends BaseSensorLogic<T> {

    protected BaseNetatamoSensorLogic(JsonPath path) {
        this.path = path;
    }

    public void process(ReadContext ctx) {
        T value = ctx.read( this.path );
        System.err.println( getClass().getSimpleName() + " sink " + value);
        sink( value );
    }

    private final JsonPath path;
}
