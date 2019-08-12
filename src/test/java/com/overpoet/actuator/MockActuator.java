package com.overpoet.actuator;

public class MockActuator<T> extends SimpleActuator<T> {

    public MockActuator(String id, Class<T> datatype) {
        super(id, datatype);
    }

    @Override
    public void actuate(T value) throws ActuationException {
        super.actuate(value);
    }
}
