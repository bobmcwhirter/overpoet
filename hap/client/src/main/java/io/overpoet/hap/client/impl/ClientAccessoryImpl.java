package io.overpoet.hap.client.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import io.overpoet.hap.client.model.ClientService;
import io.overpoet.hap.common.model.Characteristic;
import io.overpoet.hap.common.model.CharacteristicType;
import io.overpoet.hap.common.model.Characteristics;
import io.overpoet.hap.client.model.EventableCharacteristic;
import io.overpoet.hap.client.model.ClientAccessory;
import io.overpoet.hap.common.model.ServiceType;
import io.overpoet.hap.common.model.Services;

public class ClientAccessoryImpl implements ClientAccessory {

    public ClientAccessoryImpl(AccessoryDBImpl accessories, int aid) {
        this.accessories = accessories;
        this.aid = aid;
        this.listeners = new NonLeafCharacteristicListenerSet(accessories.getListeners());
    }

    @Override
    public AccessoryDBImpl getAccessoriesDB() {
        return this.accessories;
    }

    @Override
    public int getAID() {
        return this.aid;
    }

    public void addService(ClientService service) {
        this.services.add(service);
        this.listeners.stream().forEach(service::addListener);
    }

    @Override
    public List<ClientService> getServices() {
        return this.services;
    }

    @Override
    public Optional<ClientService> findService(int iid) {
        return this.services.stream().filter(e -> e.getIID() == iid).findFirst();
    }

    public Optional<ClientService> findService(ServiceType type) {
        return this.services.stream().filter(e -> e.getType() == type).findFirst();
    }

    @Override
    public Optional<Characteristic> findCharacteristic(int iid) {
        return this.services.stream().flatMap(e -> e.getCharacteristics().stream()).filter(e -> e.getIID() == iid).findFirst();
    }

    protected String getAccessoryInformation(CharacteristicType type) {
        Optional<ClientService> svc = findService(Services.ACCESSORY_INFORMATION);
        if (svc.isPresent()) {
            Optional<Characteristic> chr = svc.get().findCharacteristic(type);
            if (chr.isPresent()) {
                return (String) chr.get().getValue();
            }
        }
        return null;
    }

    @Override
    public String getManufacturer() {
        return getAccessoryInformation(Characteristics.MANUFACTURER);
    }

    @Override
    public String getModel() {
        return getAccessoryInformation(Characteristics.MODEL);
    }

    @Override
    public String getName() {
        return getAccessoryInformation(Characteristics.NAME);
    }

    @Override
    public String getHardwareRevision() {
        return getAccessoryInformation(Characteristics.HARDWARE_REVISION);
    }

    @Override
    public String getFirmwareRevision() {
        return getAccessoryInformation(Characteristics.FIRMWARE_REVISION);
    }

    @Override
    public void addListener(Consumer<EventableCharacteristic> listener) {
        this.listeners.addListener(listener);
        this.services.forEach(e -> {
            e.addListener(listener);
        });
    }

    @Override
    public void removeListener(Consumer<EventableCharacteristic> listener) {
        this.listeners.removeListener(listener);
        this.services.forEach(e -> {
            e.removeListener(listener);
        });
    }

    @Override
    public void removeAllListeners() {
        this.services.forEach(e -> {
            e.removeAllListeners();
            ;
        });

    }

    public NonLeafCharacteristicListenerSet getListeners() {
        return this.listeners;
    }

    public String toString() {
        return "[Accessory: aid=" + this.aid + "; services=" + this.services + "]";
    }

    private final int aid;

    private final List<ClientService> services = new ArrayList<>();

    private final AccessoryDBImpl accessories;

    private final NonLeafCharacteristicListenerSet listeners;
}
