package io.overpoet.hap.client.model;

import java.util.function.Consumer;

import io.overpoet.hap.common.model.EventableCharacteristic;
import io.overpoet.hap.common.model.Service;

public interface ClientService extends Service {
    void addListener(Consumer<EventableCharacteristic> listener);
    void removeListener(Consumer<EventableCharacteristic> listener);
    void removeAllListeners();

}
