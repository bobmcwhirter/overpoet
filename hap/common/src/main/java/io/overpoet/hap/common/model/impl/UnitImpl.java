package io.overpoet.hap.common.model.impl;

import io.overpoet.hap.common.model.Unit;

/**
 * Created by bob on 9/14/18.
 */
public class UnitImpl implements Unit {
    public UnitImpl(String name) {
        this.name = name;
    }
    @Override
    public String getName() {
        return this.name;
    }

    private final String name;
}
