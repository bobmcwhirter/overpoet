package io.overpoet.hap.common.model.impl;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

import io.overpoet.hap.common.model.CharacteristicType;
import io.overpoet.hap.common.model.Constants;
import io.overpoet.hap.common.model.Format;
import io.overpoet.hap.common.model.Permission;
import io.overpoet.hap.common.model.Unit;

/**
 * Created by bob on 9/13/18.
 */
public class CharacteristicTypeImpl implements CharacteristicType {

    public CharacteristicTypeImpl(UUID uuid,
                                  String type,
                                  String name,
                                  Format format,
                                  Unit unit,
                                  Number minimumValue,
                                  Number maximumValue,
                                  Number stepValue,
                                  Set<Permission> permissions) {
        this.uuid = uuid;
        this.type = type;
        this.name = name;
        this.format = format;
        this.unit = unit;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.stepValue = stepValue;
        if (permissions.isEmpty()) {
            this.permissions = EnumSet.noneOf(Permission.class);
        } else {
            this.permissions = EnumSet.copyOf(permissions);
        }
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public String getEncodedType() {
        String str = getUUID().toString().toUpperCase();
        if (str.endsWith(Constants.PRIMARY_UUID_SUFFIX)) {
            String t = str.substring(0, 8);
            int i = 0;
            for (i = 0; i < t.length(); ++i) {
                if (t.charAt(i) == '0') {
                    continue;
                } else {
                    break;
                }
            }

            return t.substring(i).toUpperCase();
        }
        return str;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public EnumSet<Permission> getPermissions() {
        return this.permissions;
    }

    @Override
    public Format getFormat() {
        return this.format;
    }

    @Override
    public Unit getUnit() {
        return this.unit;
    }

    @Override
    public Number getMinimumValue() {
        return this.minimumValue;
    }

    @Override
    public Number getMaximumValue() {
        return this.maximumValue;
    }

    @Override
    public Number getStepValue() {
        return this.stepValue;
    }

    @Override
    public String toString() {
        return "[CharacteristicType: name=" + this.name + "]";
    }

    private final UUID uuid;

    private final String type;

    private final String name;

    private final EnumSet<Permission> permissions;

    private final Format format;

    private final Unit unit;

    private Number minimumValue;

    private Number maximumValue;

    private Number stepValue;
}
