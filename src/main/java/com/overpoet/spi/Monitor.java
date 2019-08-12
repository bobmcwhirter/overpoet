package com.overpoet.spi;

import java.util.function.Consumer;

public interface Monitor {

    interface Sensor<T> {
        Class<T> datatype();
        void onChange(Consumer<T> listener);
    }

    default <T> void register(Sensor<T> sensor) {

    }

}
