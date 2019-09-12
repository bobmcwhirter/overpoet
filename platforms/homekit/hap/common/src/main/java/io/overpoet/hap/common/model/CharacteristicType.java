package io.overpoet.hap.common.model;

import java.util.EnumSet;
import java.util.UUID;

/**
 * Created by bob on 9/14/18.
 */
public interface CharacteristicType<JAVA_TYPE, FORMAT_TYPE extends Format<JAVA_TYPE>> {

    UUID getUUID();
    String getEncodedType();
    String getType();
    String getName();
    EnumSet<Permission> getPermissions();

    Unit getUnit();

    FORMAT_TYPE getFormat();

    Number getMinimumValue();
    Number getMaximumValue();
    Number getStepValue();
}
