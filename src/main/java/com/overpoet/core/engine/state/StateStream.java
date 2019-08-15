package com.overpoet.core.engine.state;

public interface StateStream {
    State currentHead();

    <T> StateStream add(Sense<T> sense) throws StateException;

    <T> StateStream add(Actuation<T> actuation) throws StateException;
}
