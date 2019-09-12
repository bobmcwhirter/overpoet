package io.overpoet.hap.common.model;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.json.JsonValue;

/**
 * Created by bob on 9/13/18.
 */
public interface Characteristic<JAVA_TYPE, FORMAT_TYPE extends Format<JAVA_TYPE>> {
    Service getService();
    int getIID();
    CharacteristicType<JAVA_TYPE, FORMAT_TYPE> getType();
    void requestValueUpdate(JAVA_TYPE value);
    void requestValueUpdate(JsonValue value);
    Object getValue();
    <T> T getValue(Class<T> asType);

    boolean isWritable();

    void setController(Consumer<JAVA_TYPE> value);
    //void addChangeRequestedListener(BiConsumer<Characteristic<JAVA_TYPE, FORMAT_TYPE>, JAVA_TYPE> listener);
    //void removeChangeRequestedListener(BiConsumer<Characteristic<JAVA_TYPE, FORMAT_TYPE>, JAVA_TYPE> listener);
    //void removeAllChangeRequesListeners();

    void addListener(Consumer<Characteristic<JAVA_TYPE, FORMAT_TYPE>> listener);
    void removeListener(Consumer<Characteristic<JAVA_TYPE, FORMAT_TYPE>> listener);
    void removeAllListeners();
}
