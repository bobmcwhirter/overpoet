package io.overpoet.core.engine.state;

public interface StateStream {
    State currentHead();

    <T> boolean add(Sense<T> sense) throws StateException;

    <T> boolean add(Actuation<T> actuation) throws StateException;
}
