package com.overpoet.sensor;

public class SimpleSensor<T> implements Sensor<T> {

    public SimpleSensor(String id, Class<T> datatype) {
        this.id = id;
        this.datatype = datatype;
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public Class<T> datatype() {
        return this.datatype;
    }

    @Override
    public void initialize(Sensor.Sink<T> sink) {
        this.sink = sink;
    }

    protected void sink(T value) throws SensorInitializationException, SenseException {
        if ( this.sink == null ) {
            throw new SensorInitializationException("Sensor sink not initialized.");
        }
        this.sink.sink(value);
    }


    private final String id;
    private final Class<T> datatype;
    private Sensor.Sink<T> sink;
}
