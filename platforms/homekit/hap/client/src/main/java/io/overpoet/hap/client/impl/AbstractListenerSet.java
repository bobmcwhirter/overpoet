package io.overpoet.hap.client.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import io.overpoet.hap.common.model.EventableCharacteristic;

public class AbstractListenerSet implements ListenerSet {

    AbstractListenerSet() {
        this(null);
    }

    AbstractListenerSet(ListenerSet parent) {
        this.parent = Optional.ofNullable(parent);
    }

    @Override
    public Optional<ListenerSet> getParent() {
        return this.parent;
    }

    @Override
    public Stream<Consumer<EventableCharacteristic>> stream() {
        if (this.parent.isPresent() ) {
            return Stream.concat( this.listeners.stream(), this.parent.get().stream() );
        }
        return this.listeners.stream();
    }

    public List<Consumer<EventableCharacteristic>> getListeners() {
        return this.listeners;
    }

    private final Optional<ListenerSet> parent;
    protected List<Consumer<EventableCharacteristic>> listeners = new ArrayList<>();

}
