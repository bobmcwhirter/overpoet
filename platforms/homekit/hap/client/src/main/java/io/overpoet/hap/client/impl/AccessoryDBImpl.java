package io.overpoet.hap.client.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import io.overpoet.hap.client.model.AccessoryDB;
import io.overpoet.hap.client.model.ClientAccessory;
import io.overpoet.hap.common.model.EventableCharacteristic;

public class AccessoryDBImpl implements AccessoryDB {

    public AccessoryDBImpl(PairedConnectionImpl pairedConnection) {
        this.pairedConnection = pairedConnection;
    }

    public PairedConnectionImpl getPairedConnection() {
        return this.pairedConnection;
    }

    @Override
    public Optional<ClientAccessory> find(int aid) {
        return this.accessories.stream().filter(e -> e.getAID() == aid).findFirst();
    }

    public void addAccessory(ClientAccessory accessory) {
        this.accessories.add(accessory);
        this.listeners.stream().forEach(accessory::addListener);
    }

    @Override
    public List<ClientAccessory> accessories() {
        return this.accessories;
    }

    public void addListener(Consumer<EventableCharacteristic> listener) {
        this.listeners.addListener( listener );
        this.accessories.forEach(e->{
            e.addListener(listener);

        });
    }

    public void removeListener(Consumer<EventableCharacteristic> listener) {
        this.listeners.removeListener( listener );
        this.accessories.forEach( e->{
            e.removeListener(listener);
        });
    }

    public void removeAllListeners() {
        this.listeners.removeAllListeners();
        this.accessories.forEach( e->{
            e.removeAllListeners();
        });
    }

    public ListenerSet getListeners() {
        return listeners;
    }

    public String toString() {
        return "[Accessories " + this.accessories + "]";
    }

    private final PairedConnectionImpl pairedConnection;

    private List<ClientAccessory> accessories = new ArrayList<>();

    private NonLeafCharacteristicListenerSet listeners = new NonLeafCharacteristicListenerSet();

}
