package io.overpoet.hap.common.model;

import java.util.List;
import java.util.Optional;

public interface Service {
    Accessory getAccessory();
    int getIID();
    ServiceType getType();
    List<Characteristic> getCharacteristics();
    Optional<Characteristic> findCharacteristic(CharacteristicType type);

    String getName();
}
