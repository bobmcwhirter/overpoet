package io.overpoet.hap.client.impl;

import java.util.function.Consumer;

import io.overpoet.hap.client.model.AccessoryDB;
import io.overpoet.hap.client.model.ClientAccessory;
import io.overpoet.hap.client.model.ClientService;
import io.overpoet.hap.common.model.EventableCharacteristic;
import io.overpoet.hap.common.model.impl.AbstractAccessoryImpl;

public class ClientAccessoryImpl extends AbstractAccessoryImpl<ClientService> implements ClientAccessory {

    public ClientAccessoryImpl(AccessoryDBImpl accessories, int aid) {
        super(aid);
        this.accessories = accessories;
        this.listeners = new NonLeafCharacteristicListenerSet(accessories.getListeners());
    }

    @Override
    public AccessoryDB getAccessoriesDB() {
        return this.accessories;
    }

    @Override
    public void addListener(Consumer<EventableCharacteristic> listener) {
        this.listeners.addListener(listener);
        getServices().forEach(e -> {
            e.addListener(listener);
        });
    }

    @Override
    public void removeListener(Consumer<EventableCharacteristic> listener) {
        this.listeners.removeListener(listener);
        getServices().forEach(e -> {
            e.removeListener(listener);
        });
    }

    @Override
    public void removeAllListeners() {
        getServices().forEach(e -> {
            e.removeAllListeners();
            ;
        });

    }

    public NonLeafCharacteristicListenerSet getListeners() {
        return this.listeners;
    }

    private final AccessoryDBImpl accessories;

    private final NonLeafCharacteristicListenerSet listeners;
}
