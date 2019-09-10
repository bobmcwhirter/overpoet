package io.overpoet.spi.aspect;

import io.overpoet.spi.Keyed;
import io.overpoet.spi.actuator.Actuator;
import io.overpoet.spi.metadata.Metadata;
import io.overpoet.spi.sensor.Sensor;

public interface Aspect<T,MT extends Metadata<T>> extends Keyed {
    Class<T> datatype();

    MT metadata();

    default Sensor<T> sensor() {
        return null;
    }

    default <C> Sensor<C> sensor(Class<C> type) {
        if ( type != datatype() ) {
            throw new IllegalArgumentException("Sensor can only be cast to " + datatype());
        }
        return (Sensor<C>) sensor();
    }

    default Actuator<T> actuator() {
        return null;
    }

    default <C> Actuator<C> actuator(Class<C> type) {
        if ( type != datatype() ) {
            throw new IllegalArgumentException("Actuator can only be cast to " + datatype());
        }
        return (Actuator<C>) actuator();

    }

    default boolean isSensor() {
        return sensor() != null;
    }

    default boolean isActuator() {
        return actuator() != null;
    }
}
