package io.overpoet.hap.common.model;

import java.util.Set;
import java.util.UUID;

public interface ServiceType {
    String getName();
    UUID getUUID();
    String getEncodedType();
    String getType();
    Set<CharacteristicType> getRequiredCharacteristics();
    Set<CharacteristicType> getOptionalCharacteristics();

}
