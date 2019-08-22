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

public class ServiceImpl implements Service {

    public ServiceImpl(Accessory accessory, int iid, ServiceType type) {
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

    public void addCharacteristic(Characteristic characteristic) {
        this.characteristics.add(characteristic);
    }

    @Override
    public List<Characteristic> getCharacteristics() {
        return this.characteristics;
    }

    @Override
    public Optional<Characteristic> findCharacteristic(CharacteristicType type) {
        return this.characteristics.stream().filter(e->e.getType() == type).findFirst();
    }

    @Override
    public String getName() {
        Optional<Characteristic> chr = findCharacteristic(Characteristics.NAME);
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

    private List<Characteristic> characteristics = new ArrayList<>();
}
