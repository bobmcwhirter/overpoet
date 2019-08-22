package io.overpoet.hap.common.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import io.overpoet.hap.common.model.Accessory;
import io.overpoet.hap.common.model.Characteristic;
import io.overpoet.hap.common.model.CharacteristicType;
import io.overpoet.hap.common.model.Characteristics;
import io.overpoet.hap.common.model.Service;
import io.overpoet.hap.common.model.ServiceType;
import io.overpoet.hap.common.model.Services;

public abstract class AbstractAccessoryImpl<SERVICE_TYPE extends Service> implements Accessory {

    public AbstractAccessoryImpl(int aid) {
        this.aid = aid;
    }

    @Override
    public int getAID() {
        return this.aid;
    }

    public void addService(SERVICE_TYPE service) {
        this.services.add(service);
    }

    @Override
    public List<SERVICE_TYPE> getServices() {
        return this.services;
    }

    @Override
    public Optional<SERVICE_TYPE> findService(int iid) {
        return this.services.stream().filter(e -> e.getIID() == iid).findFirst();
    }

    public Optional<SERVICE_TYPE> findService(ServiceType type) {
        return this.services.stream().filter(e -> e.getType() == type).findFirst();
    }

    @Override
    public Optional<? extends Characteristic> findCharacteristic(int iid) {
        return this.services.stream().flatMap(e -> e.getCharacteristics().stream()).filter(e -> e.getIID() == iid).findFirst();
    }

    protected String getAccessoryInformation(CharacteristicType type) {
        Optional<SERVICE_TYPE> svc = findService(Services.ACCESSORY_INFORMATION);
        if (svc.isPresent()) {
            Optional<? extends Characteristic> chr = svc.get().findCharacteristic(type);
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

    public String toString() {
        return "[Accessory: aid=" + this.aid + "; services=" + this.services + "]";
    }

    private final int aid;

    private final List<SERVICE_TYPE> services = new ArrayList<>();
}
