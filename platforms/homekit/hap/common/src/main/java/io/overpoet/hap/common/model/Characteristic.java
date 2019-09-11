package io.overpoet.hap.common.model;

/**
 * Created by bob on 9/13/18.
 */
public interface Characteristic {
    Service getService();
    int getIID();
    CharacteristicType getType();
    void updateValue(Object value);
    void requestValueUpdate(Object value);
    Object getValue();
    <T> T getValue(Class<T> asType);

    boolean isWritable();
}
