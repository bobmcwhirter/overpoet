package io.overpoet.hap.client.codec;

import java.util.concurrent.CompletableFuture;

import io.overpoet.hap.client.model.EventableCharacteristic;
import io.overpoet.hap.common.model.Characteristic;

public class CharacteristicEventsRequest implements SyncRequest {

    public CharacteristicEventsRequest(EventableCharacteristic characteristic, boolean enable) {
        this.characteristic = characteristic;
        this.enable = enable;
        this.future = new CompletableFuture<>();
    }

    public CompletableFuture<Object> getFuture() {
        return this.future;
    }

    public EventableCharacteristic getCharacteristic() {
        return this.characteristic;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public boolean isDisable() {
        return ! this.enable;
    }

    public boolean getValue() {
        return this.enable;
    }

    private final EventableCharacteristic characteristic;
    private final boolean enable;

    private final CompletableFuture<Object> future;
}
