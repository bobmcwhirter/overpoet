package io.overpoet.hap.common.model.impl;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import io.overpoet.hap.common.model.Permission;
import io.overpoet.hap.common.model.Permissions;


/**
 * Created by bob on 9/14/18.
 */
public class PermissionsImpl implements Permissions {

    public PermissionsImpl() {

    }

    public PermissionsImpl(Set<Permission> permissions) {
        this.permissions.addAll(permissions);
    }

    @Override
    public boolean contains(Permission permission) {
        return this.permissions.contains(permission);
    }

    @Override
    public EnumSet<Permission> values() {
        return permissions.clone();
    }

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }

    @Override
    public String toString() {
        return "[Permissions: " + this.permissions + "]";
    }

    private EnumSet<Permission> permissions = EnumSet.noneOf(Permission.class);
}
