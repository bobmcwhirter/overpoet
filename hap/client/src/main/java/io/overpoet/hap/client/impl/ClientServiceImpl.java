package io.overpoet.hap.client.impl;

import java.util.function.Consumer;

import io.overpoet.hap.client.model.ClientService;
import io.overpoet.hap.client.model.EventableCharacteristic;
import io.overpoet.hap.common.model.Characteristic;
import io.overpoet.hap.common.model.ServiceType;
import io.overpoet.hap.common.model.impl.AbstractServiceImpl;

public class ClientServiceImpl extends AbstractServiceImpl implements ClientService {

    public ClientServiceImpl(ClientAccessoryImpl accessory, int iid, ServiceType type) {
        super( accessory, iid, type);
        this.listeners = new NonLeafCharacteristicListenerSet(accessory.getListeners());
    }

    public void addCharacteristic(Characteristic characteristic) {
        super.addCharacteristic(characteristic);
        if (characteristic instanceof EventableCharacteristic) {
            this.listeners.stream().forEach(((EventableCharacteristic) characteristic)::addListener);
        }
    }

    public void addListener(Consumer<EventableCharacteristic> listener) {
        getCharacteristics().forEach(e -> {
            if (e instanceof EventableCharacteristicImpl) {
                ((EventableCharacteristicImpl) e).addListener(listener);
            }
        });
    }

    @Override
    public void removeListener(Consumer<EventableCharacteristic> listener) {
        getCharacteristics().forEach(e -> {
            if (e instanceof EventableCharacteristicImpl) {
                ((EventableCharacteristicImpl) e).removeListener(listener);
            }
        });
    }

    @Override
    public void removeAllListeners() {
        getCharacteristics().forEach(e -> {
            if (e instanceof EventableCharacteristicImpl) {
                ((EventableCharacteristicImpl) e).removeAllListeners();
            }
        });
    }

    private NonLeafCharacteristicListenerSet listeners;
}
