package com.overpoet.core;

import com.overpoet.spi.Manipulator;

class WrappedManipulator implements Manipulator {

    WrappedManipulator(Manipulator delegate) {
        this.delegate = delegate;
    }

    private final Manipulator delegate;
}
