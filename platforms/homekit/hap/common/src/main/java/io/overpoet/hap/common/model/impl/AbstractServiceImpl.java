package io.overpoet.hap.common.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.overpoet.hap.common.model.Accessory;
import io.overpoet.hap.common.model.Characteristic;
import io.overpoet.hap.common.model.CharacteristicType;
import io.overpoet.hap.common.model.Characteristics;
import io.overpoet.hap.common.model.Service;
import io.overpoet.hap.common.model.ServiceType;

public class AbstractServiceImpl<CHARACTERISTIC_TYPE extends Characteristic> implements Service {

    public AbstractServiceImpl(Accessory accessory, int iid, ServiceType type) {
        this.accessory = accessory;
        this.iid = iid;
        this.type = type;
    }

    @Override
    public Accessory getAccessory() {
        return this.accessory;
    }

    @Override
    public int getIID() {
        return this.iid;
    }

    @Override
    public ServiceType getType() {
        return this.type;
    }

    public void addCharacteristic(CHARACTERISTIC_TYPE characteristic) {
        this.characteristics.add(characteristic);
    }

    @Override
    public List<CHARACTERISTIC_TYPE> getCharacteristics() {
        return this.characteristics;
    }

    @Override
    public Optional<CHARACTERISTIC_TYPE> findCharacteristic(CharacteristicType type) {
        return this.characteristics.stream().filter(e->e.getType() == type).findFirst();
    }

    @Override
    public String getName() {
        Optional<CHARACTERISTIC_TYPE> chr = findCharacteristic(Characteristics.NAME);
        if ( chr.isPresent() ) {
            return (String) chr.get().getValue();
        }
        return this.type.getName();
    }

    @Override
    public String toString() {
        return "[Service: iid=" + this.iid + "; type=" + this.type + "; characteristics=" + this.characteristics + "]";
    }

    private final int iid;

    private final ServiceType type;

    private final Accessory accessory;

    private List<CHARACTERISTIC_TYPE> characteristics = new ArrayList<>();
}
