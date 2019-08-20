package com.overpoet.hap.client.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.overpoet.hap.client.PairedConnection;

/**
 * Created by bob on 9/10/18.
 */
public interface Accessories {
    List<Accessory> accessories();
    PairedConnection getPairedConnection();
    Optional<Accessory> find(int aid);
    void addListener(Consumer<EventableCharacteristic> listener);
    void removeListener(Consumer<EventableCharacteristic> listener);
    void removeAllListeners();
}
