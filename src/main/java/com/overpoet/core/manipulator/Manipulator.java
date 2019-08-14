package com.overpoet.core.manipulator;

import java.util.Set;

import com.overpoet.Identified;
import com.overpoet.Keyed;
import com.overpoet.core.actuator.ActuationException;
import com.overpoet.core.sensor.Sensor;

public interface Manipulator {

    interface Apparatus extends Identified, Keyed {
        Set<Sensor<?>> sensors();
        Set<Actuator<?>> actuators();
    }

    interface Actuator<T> extends Identified, Keyed {
        Class<T> datatype();
        void actuate(T value) throws ActuationException;
    }

    void register(Apparatus apparatus);
}
