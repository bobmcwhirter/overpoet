package com.overpoet.core.sensor;

import com.overpoet.Key;
import com.overpoet.core.metadata.EnumMetadata;

public class EnumSensor<E extends Enum<E>> extends AbstractSensor<E,EnumMetadata<E>> {

    EnumSensor(Key key, Class<E> datatype, EnumMetadata<E> metadata, SensorLogic<E> logic) {
        super(key, datatype, metadata, logic);
    }
}
