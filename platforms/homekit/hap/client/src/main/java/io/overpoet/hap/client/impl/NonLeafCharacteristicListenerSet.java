package io.overpoet.hap.client.impl;

import java.util.function.Consumer;

import io.overpoet.hap.common.model.EventableCharacteristic;

public class NonLeafCharacteristicListenerSet extends AbstractListenerSet {

    public NonLeafCharacteristicListenerSet() {
        this(null);
    }

    public NonLeafCharacteristicListenerSet(ListenerSet parent) {
        super(parent);
    }

    public void addListener(Consumer<EventableCharacteristic> listener) {
        this.listeners.add( listener );
    }

    public void removeListener(Consumer<EventableCharacteristic> listener) {
        this.listeners.remove(listener);
    }

    public void removeAllListeners() {
        this.listeners.clear();
    }

}
