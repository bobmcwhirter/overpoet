package io.overpoet.core.sensor;

import io.overpoet.Key;
import io.overpoet.core.metadata.StringMetadata;

public class StringSensor extends AbstractSensor<String, StringMetadata> {
    public StringSensor(Key key, StringMetadata metadata) {
        this(key, metadata, null);
    }

    public StringSensor(Key key, StringMetadata metadata, SensorLogic<String> logic) {
        super(key, String.class, metadata, logic);
    }
}
