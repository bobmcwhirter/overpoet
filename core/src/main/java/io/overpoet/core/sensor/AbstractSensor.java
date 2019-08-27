package io.overpoet.core.sensor;

import io.overpoet.Key;
import io.overpoet.core.metadata.Metadata;

abstract class AbstractSensor<T,MT extends Metadata<T>> implements Sensor<T> {

    AbstractSensor(Key key, Class<T> datatype, MT metadata, SensorLogic<T> logic) {
        this.key = key;
        this.datatype = datatype;
        this.metadata = metadata;
        this.logic = logic;
        this.logic.start(this::sink);
    }

    private void sink(T value) {
        if ( this.sink != null ) {
            this.sink.sink(value);
        }
        this.lastValue = value;
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
    public void onChange(Sink<T> sink) {
        System.err.println( "adding onChange to " + this);
        System.err.println( " percolate: " + this.lastValue);
        this.sink = sink;
        if ( this.lastValue != null ) {
            this.sink.sink(this.lastValue);
        }
    }

    @Override
    public Key key() {
        return this.key;
    }

    private final Key key;
    private final Class<T> datatype;
    private final SensorLogic<T> logic;
    private final MT metadata;

    private Sink<T> sink;
    private T lastValue;
}
