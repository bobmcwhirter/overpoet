package com.overpoet.core.metadata;

public class EnumMetadata<E extends Enum<E>> implements Metadata<E> {

    public EnumMetadata(Class<E> enumType) {
        this.enumType = enumType;
    }

    public E[] possibleValues() {
        return this.enumType.getEnumConstants();
    }

    private final Class<E> enumType;
}
