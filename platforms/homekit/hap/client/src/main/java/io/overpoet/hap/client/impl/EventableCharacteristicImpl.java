package io.overpoet.hap.client.impl;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import io.overpoet.hap.client.model.ClientAccessory;
import io.overpoet.hap.common.model.EventableCharacteristic;
import io.overpoet.hap.common.model.CharacteristicType;
import io.overpoet.hap.common.model.impl.AbstractCharacteristicImpl;

public abstract class EventableCharacteristicImpl extends AbstractCharacteristicImpl implements EventableCharacteristic {

    public EventableCharacteristicImpl(ClientServiceImpl service, int iid, CharacteristicType type) {
        super(service, iid, type);
        this.listeners = new LeafCharacteristicListenerSet();
    }

    @Override
    public void addChangeListener(Consumer<EventableCharacteristic> listener) {
        try {
            this.listeners.addListener(this, listener);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeChangeListener(Consumer<EventableCharacteristic> listener) {
        try {
            this.listeners.removeListener(this, listener);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeAllChangeListeners() {
        try {
            this.listeners.removeAllListeners(this);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void fireEvent(Object newValue) {
        setStoredValue(newValue);
        this.listeners.fireEvent(this);
    }

    @Override
    public void updateValue(Object value) {

    }

    void enableEvents() throws ExecutionException, InterruptedException {
        ((PairedConnectionImpl)((ClientAccessory)getService().getAccessory()).getAccessoriesDB().getPairedConnection()).enableEvents(this);
    }

    void disableEvents() throws ExecutionException, InterruptedException {
        ((PairedConnectionImpl)((ClientAccessory)getService().getAccessory()).getAccessoriesDB().getPairedConnection()).disableEvents(this);
    }

    @Override
    public String toString() {
        return "[EventableCharacteristic: " + super.toString() + "]";
    }

    private LeafCharacteristicListenerSet listeners;

}
