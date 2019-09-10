package io.overpoet.spi.aspect;

import io.overpoet.spi.Key;
import io.overpoet.spi.actuator.Actuator;
import io.overpoet.spi.metadata.Metadata;
import io.overpoet.spi.sensor.Sensor;

public class AspectBuilder<T, MT extends Metadata<T>> implements Aspect<T, MT> {

    public AspectBuilder(Key key, Class<T> datatype, MT metadata) {
        this.key = key;
        this.datatype = datatype;
        this.metadata = metadata;
    }

    @Override
    public Class<T> datatype() {
        return this.datatype;
    }

    @Override
    public MT metadata() {
        return this.metadata;
    }

    @Override
    public Key key() {
        return this.key;
    }

    @Override
    public Sensor<T> sensor() {
        return this.sensor;
    }

    @Override
    public Actuator<T> actuator() {
        return this.actuator;
    }

    public AspectBuilder<T, MT> withSensor(Sensor<T> sensor) {
        this.sensor = sensor;
        return this;
    }

    public AspectBuilder<T, MT> withActuator(Actuator<T> actuator) {
        this.actuator = actuator;
        return this;
    }

    public static <T, MT extends Metadata<T>> AspectBuilder<T, MT> of(Key key, Class<T> datatype, MT metadata) {
        return new AspectBuilder<>(key, datatype, metadata);
    }

    @Override
    public String toString() {
        return "[Aspect " + this.key + " " + datatype +" s=" + this.sensor + "; a=" + this.actuator + "]";
    }

    private final Key key;

    private final Class<T> datatype;

    private final MT metadata;

    private Sensor<T> sensor;

    private Actuator<T> actuator;
}
