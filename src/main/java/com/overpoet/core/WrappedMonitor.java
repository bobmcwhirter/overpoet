package com.overpoet.core;

import com.overpoet.spi.Monitor;

class WrappedMonitor implements Monitor {

    WrappedMonitor(Monitor delegate) {
        this.delegate = delegate;
    }

    private final Monitor delegate;
}
