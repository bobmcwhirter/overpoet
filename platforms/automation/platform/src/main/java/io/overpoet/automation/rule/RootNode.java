package io.overpoet.automation.rule;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.overpoet.spi.Key;

class RootNode {

    RootNode() {

    }

    <T> void assertSensor(Key key, T value) {
        Agenda agenda = new Agenda();
        getSensorNode(key).assertValue(agenda, value);
        agenda.process(Throwable::printStackTrace);
    }

    <T> SensorNode<T> getSensorNode(Key key) {
        return (SensorNode<T>) sensorNodes.computeIfAbsent(key, (mapKey) -> new SensorNode<T>(key));
    }

    private ConcurrentMap<Key, SensorNode<?>> sensorNodes = new ConcurrentHashMap<>();
}
