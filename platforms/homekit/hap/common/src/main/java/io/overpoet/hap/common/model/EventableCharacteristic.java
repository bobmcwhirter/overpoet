package io.overpoet.hap.common.model;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface EventableCharacteristic extends Characteristic {

    void addChangeRequestedListener(BiConsumer<EventableCharacteristic, Object> listener);
    void removeChangeRequestedListener(BiConsumer<EventableCharacteristic, Object> listener);
    void removeAllChangeRequesListeners();

    void addChangeListener(Consumer<EventableCharacteristic> listener);
    void removeChangeListener(Consumer<EventableCharacteristic> listener);
    void removeAllChangeListeners();

}
