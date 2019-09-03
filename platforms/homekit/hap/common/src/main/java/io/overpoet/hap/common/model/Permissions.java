package io.overpoet.hap.common.model;

import java.util.EnumSet;

/**
 * Created by bob on 9/14/18.
 */
public interface Permissions {
    boolean contains(Permission permission);
    EnumSet<Permission> values();
}
