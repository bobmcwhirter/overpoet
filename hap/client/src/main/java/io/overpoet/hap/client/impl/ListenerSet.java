package io.overpoet.hap.client.impl;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import io.overpoet.hap.client.model.EventableCharacteristic;

public interface ListenerSet {
    Optional<ListenerSet> getParent();
    Stream<Consumer<EventableCharacteristic>> stream();
}
