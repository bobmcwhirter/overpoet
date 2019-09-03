package io.overpoet.hap.common.model;

import java.util.List;
import java.util.Optional;

public interface Service {
    Accessory getAccessory();
    int getIID();
    ServiceType getType();
    List<? extends Characteristic> getCharacteristics();
    Optional<? extends Characteristic> findCharacteristic(CharacteristicType type);

    String getName();
}
