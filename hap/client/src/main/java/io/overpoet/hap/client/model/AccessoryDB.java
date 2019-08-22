package io.overpoet.hap.client.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import io.overpoet.hap.client.PairedConnection;

/**
 * Created by bob on 9/10/18.
 */
public interface AccessoryDB {
    List<ClientAccessory> accessories();
    PairedConnection getPairedConnection();
    Optional<ClientAccessory> find(int aid);
    void addListener(Consumer<EventableCharacteristic> listener);
    void removeListener(Consumer<EventableCharacteristic> listener);
    void removeAllListeners();
}
