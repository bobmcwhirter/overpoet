package com.overpoet.core.manipulator;

import java.util.Set;
import java.util.function.Consumer;

import com.overpoet.Identified;
import com.overpoet.Keyed;
import com.overpoet.core.actuator.ActuationException;

public interface Manipulator {

    interface Apparatus extends Identified, Keyed {
        Set<Sensor<?>> sensors();
        Set<Actuator<?>> actuators();
    }

    interface Actuator<T> extends Identified, Keyed {
        Class<T> datatype();
        void actuate(T value) throws ActuationException;
    }

    interface Sensor<T> extends Identified, Keyed {
        Class<T> datatype();
        void onChange(Consumer<T> listener);
    }

    void register(Apparatus apparatus);
}
