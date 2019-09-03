package io.overpoet.engine.engine.state;

import io.overpoet.spi.sensor.Sensor;

public interface State {
    <T> State add(Sense<T> sense) throws StateException;

    <T> State add(Actuation<T> actuation) throws StateException;

    <T> T value(Sensor<T> sensor);
}
