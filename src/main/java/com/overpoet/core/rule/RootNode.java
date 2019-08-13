package com.overpoet.core.rule;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.overpoet.Key;

class RootNode {

    RootNode() {

    }

    <T> void assertSensor(Key key, T value) {
        Agenda agenda = new Agenda();
        getSensorNode(key).assertValue(agenda, value);
        agenda.process(Throwable::printStackTrace);
    }

    @SuppressWarnings("unchecked")
    <T> SensorNode<T> getSensorNode(Key key) {
        return (SensorNode<T>) inputNodes.computeIfAbsent(key, (mapKey) -> new SensorNode<T>(key));
    }

    private ConcurrentMap<Key, SensorNode<?>> inputNodes = new ConcurrentHashMap<>();

}
