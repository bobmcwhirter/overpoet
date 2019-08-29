package io.overpoet.core.ui.impl;

import io.overpoet.core.ui.BaseHandler;

public class Dispatch<T extends BaseHandler<?>> {

    Dispatch(PathMatch match, T handler) {
        this.match = match;
        this.handler = handler;
    }

    public PathMatch match() {
        return this.match;
    }

    public T handler() {
        return this.handler;
    }

    private final PathMatch match;

    private final T handler;
}
