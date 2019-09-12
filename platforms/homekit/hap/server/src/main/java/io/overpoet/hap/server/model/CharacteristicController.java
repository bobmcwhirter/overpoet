package io.overpoet.hap.server.model;

public interface CharacteristicController<JAVA_TYPE> {
    void onChange(JAVA_TYPE value);
}
