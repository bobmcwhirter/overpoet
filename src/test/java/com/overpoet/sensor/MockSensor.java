package com.overpoet.sensor;

public class MockSensor<T> extends SimpleSensor<T> {
    public MockSensor(String id, Class<T> datatype) {
        super(id, datatype);
    }

    @Override
    public void sink(T value) throws SensorInitializationException, SenseException {
        super.sink(value);
    }
}
