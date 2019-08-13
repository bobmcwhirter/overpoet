package com.overpoet.core;

import com.overpoet.Identified;

public abstract class AbstractIdentified implements Identified {

    protected AbstractIdentified(String id) {
        this.id = id;
    }

    @Override
    public String id() {
        return this.id;
    }

    private String id;
}
