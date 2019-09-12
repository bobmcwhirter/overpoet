package io.overpoet.hap.common.model.impl;

import java.util.Arrays;
import java.util.EnumSet;

import io.overpoet.hap.common.model.Characteristic;
import io.overpoet.hap.common.model.CharacteristicType;
import io.overpoet.hap.common.model.Format;
import io.overpoet.hap.common.model.Permission;
import io.overpoet.hap.common.model.Service;

public abstract class AbstractCharacteristicImpl<JAVA_TYPE, FORMAT_TYPE extends Format<JAVA_TYPE>> implements Characteristic<JAVA_TYPE, FORMAT_TYPE> {
    public AbstractCharacteristicImpl(Service service, int iid, CharacteristicType type) {
        this.service = service;
        this.iid = iid;
        this.type = type;
    }

    @Override
    public Service getService() {
        return this.service;
    }

    @Override
    public int getIID() {
        return this.iid;
    }

    @Override
    public CharacteristicType<JAVA_TYPE, FORMAT_TYPE> getType() {
        return this.type;
    }

    public void setStoredValue(JAVA_TYPE value) {
        this.value = value;
    }

    /*
    public void updateValue(Object value) {
        System.err.println( "SHOOT AN UPDATE of " + this + " -> " + value);
        try {
            getService().getAccessory().getAccessoriesDB().getPairedConnection().updateValue(this, value);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
     */

    public JAVA_TYPE getValue() {
        return this.value;
    }

    @Override
    public <T> T getValue(Class<T> asType) {
        if ( asType.isInstance(this.value)) {
            return asType.cast(this.value);
        }
        throw new RuntimeException("invalid cast: " + this.value + " to " + asType);
    }

    @Override
    public boolean isWritable() {
        return getType().getPermissions().contains(Permission.PAIRED_WRITE);
    }

    public void setPermissions(Permission...permissions) {
        setPermissions(EnumSet.copyOf(Arrays.asList(permissions)));
    }

    public void setPermissions(EnumSet<Permission> permissions) {
        this.permissions = permissions;
    }

    public EnumSet<Permission> getPermissions() {
        if ( this.permissions.isEmpty() ) {
            return this.getType().getPermissions();
        }
        return this.permissions;
    }

    @Override
    public String toString() {
        return "[Characteristics: iid=" + this.iid + "; type=" + this.type + "; value=" + this.value + "; perms=" + this.permissions + "]";
    }

    private final int iid;

    private final CharacteristicType<JAVA_TYPE, FORMAT_TYPE> type;

    private final Service service;

    private EnumSet<Permission> permissions = EnumSet.noneOf(Permission.class);

    private JAVA_TYPE value;
}
