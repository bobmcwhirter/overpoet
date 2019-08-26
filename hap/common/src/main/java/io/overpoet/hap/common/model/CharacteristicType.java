package io.overpoet.hap.common.model;

import java.util.UUID;

/**
 * Created by bob on 9/14/18.
 */
public interface CharacteristicType {

    UUID getUUID();
    String getEncodedType();
    String getType();
    String getName();
    Permissions getPermissions();

    Unit getUnit();

    Format getFormat();

    Number getMinimumValue();
    Number getMaximumValue();
    Number getStepValue();
}
