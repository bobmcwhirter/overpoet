package com.overpoet.core.sensor;

import com.overpoet.Key;
import com.overpoet.core.metadata.StringMetadata;

public class StringSensor extends AbstractSensor<String, StringMetadata> {
    public StringSensor(Key key, StringMetadata metadata) {
        this(key, metadata, null);
    }

    public StringSensor(Key key, StringMetadata metadata, SensorLogic<String> logic) {
        super(key, String.class, metadata, logic);
    }
}
