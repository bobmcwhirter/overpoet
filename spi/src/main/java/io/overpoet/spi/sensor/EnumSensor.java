package io.overpoet.spi.sensor;

import io.overpoet.spi.Key;
import io.overpoet.spi.metadata.EnumMetadata;

public class EnumSensor<E extends Enum<E>> extends AbstractSensor<E, EnumMetadata<E>> {

    EnumSensor(Key key, Class<E> datatype, EnumMetadata<E> metadata, SensorLogic<E> logic) {
        super(key, datatype, metadata, logic);
    }
}
