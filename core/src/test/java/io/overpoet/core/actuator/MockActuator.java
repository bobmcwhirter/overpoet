package io.overpoet.core.actuator;

import io.overpoet.Key;

public class MockActuator<T> extends SimpleActuator<T> {

    public MockActuator(Key key, Class<T> datatype) {
        super(key, datatype);
    }

    @Override
    public void actuate(T value) throws ActuationException {
        super.actuate(value);
    }
}
