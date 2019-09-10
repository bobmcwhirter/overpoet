package io.overpoet.automation.rule;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.overpoet.spi.TypedKey;

class RootNode {

    RootNode() {

    }

    <T> void assertSensor(TypedKey<T> key, T value) {
        Agenda agenda = new Agenda();
        getSensorNode(key).assertValue(agenda, value);
        agenda.process(Throwable::printStackTrace);
    }

    <T> SensorNode<T> getSensorNode(TypedKey<T> key) {
        return (SensorNode<T>) sensorNodes.computeIfAbsent(key, (mapKey) -> new SensorNode<T>(key));
    }

    private ConcurrentMap<TypedKey<?>, SensorNode<?>> sensorNodes = new ConcurrentHashMap<>();
}
