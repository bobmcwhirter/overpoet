package io.overpoet.hap.client.model;

import java.util.function.Consumer;

import io.overpoet.hap.common.model.Characteristic;

public interface EventableCharacteristic extends Characteristic {

    void addListener(Consumer<EventableCharacteristic> listener);
    void removeListener(Consumer<EventableCharacteristic> listener);
    void removeAllListeners();

}
