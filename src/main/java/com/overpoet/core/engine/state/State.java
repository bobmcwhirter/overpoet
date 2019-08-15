package com.overpoet.core.engine.state;

import com.overpoet.core.sensor.Sensor;

public interface State {
    <T> State add(Sense<T> sense) throws StateException;

    <T> State add(Actuation<T> actuation) throws StateException;

    <T> T value(Sensor<T> sensor);
}
