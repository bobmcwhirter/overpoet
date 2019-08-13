package com.overpoet.core.actuator;

public class SimpleActuator<T> implements Actuator<T> {

    protected SimpleActuator(String id, Class<T> datatype) {
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
    public void actuate(T value) throws ActuationException {
        // intentional no-op.
    }

    private final String id;
    private final Class<T> datatype;
}
