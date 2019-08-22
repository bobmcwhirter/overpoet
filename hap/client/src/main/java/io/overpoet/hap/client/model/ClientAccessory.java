package io.overpoet.hap.client.model;

import java.util.function.Consumer;

import io.overpoet.hap.common.model.Accessory;

/**
 * Created by bob on 9/10/18.
 */
public interface ClientAccessory extends Accessory {
    AccessoryDB getAccessoriesDB();

    void addListener(Consumer<EventableCharacteristic> listener);
    void removeListener(Consumer<EventableCharacteristic> listener);
    void removeAllListeners();
}
