package io.overpoet.hap.common.model.impl;

import io.overpoet.hap.common.model.Characteristic;
import io.overpoet.hap.common.model.CharacteristicType;
import io.overpoet.hap.common.model.Permission;
import io.overpoet.hap.common.model.Permissions;
import io.overpoet.hap.common.model.Service;

public abstract class AbstractCharacteristicImpl implements Characteristic {
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
    public CharacteristicType getType() {
        return this.type;
    }

    public void setStoredValue(Object value) {
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

    public Object getValue() {
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
        PermissionsImpl p = new PermissionsImpl();
        for (Permission permission : permissions) {
            p.addPermission(permission);
        }
        setPermissions(p);
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public Permissions getPermissions() {
        return this.permissions;
    }

    @Override
    public String toString() {
        return "[Characteristics: iid=" + this.iid + "; type=" + this.type + "; value=" + this.value + "; perms=" + this.permissions + "]";
    }

    private final int iid;

    private final CharacteristicType type;

    private final Service service;

    private Permissions permissions;

    private Object value;
}
