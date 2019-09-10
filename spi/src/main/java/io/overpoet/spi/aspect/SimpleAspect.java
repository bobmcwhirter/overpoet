package io.overpoet.spi.aspect;

import io.overpoet.spi.Key;
import io.overpoet.spi.actuator.Actuator;
import io.overpoet.spi.metadata.Metadata;
import io.overpoet.spi.sensor.Sensor;

public class SimpleAspect<T, MT extends Metadata<T>> implements Aspect<T, MT> {
    public SimpleAspect(Key key, Class<T> datatype, MT metadata, Sensor<T> sensor, Actuator<T> actuator) {
        this.key = key;
        this.datatype = datatype;
        this.metadata = metadata;
        this.sensor = sensor;
        this.actuator = actuator;
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

    private final Key key;

    private final Class<T> datatype;

    private final MT metadata;

    private final Sensor<T> sensor;

    private final Actuator<T> actuator;

}
