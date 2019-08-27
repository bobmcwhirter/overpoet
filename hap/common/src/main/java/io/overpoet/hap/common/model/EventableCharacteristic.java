package io.overpoet.hap.common.model;

import java.util.function.Consumer;

public interface EventableCharacteristic extends Characteristic {

    void addListener(Consumer<EventableCharacteristic> listener);
    void removeListener(Consumer<EventableCharacteristic> listener);
    void removeAllListeners();

}
