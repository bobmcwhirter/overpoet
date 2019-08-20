package io.overpoet.core.actuator;

import io.overpoet.Key;

public class SimpleActuator<T> implements Actuator<T> {

    protected SimpleActuator(Key key, Class<T> datatype) {
        this.key = key;
        this.datatype = datatype;
    }

    @Override
    public Key key() {
        return this.key;
    }

    @Override
    public Class<T> datatype() {
        return this.datatype;
    }

    @Override
    public void actuate(T value) throws ActuationException {
        // intentional no-op.
    }

    private final Key key;
    private final Class<T> datatype;
}
