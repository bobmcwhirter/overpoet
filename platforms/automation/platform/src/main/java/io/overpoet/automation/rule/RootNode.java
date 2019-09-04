package io.overpoet.automation.rule;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.overpoet.spi.Key;
import io.overpoet.spi.sensor.Sensor;

class RootNode {

    RootNode() {

    }

    <T> void assertSensor(Sensor<T> sensor, T value) {
        Agenda agenda = new Agenda();
        getSensorNode(sensor).assertValue(agenda, value);
        agenda.process(Throwable::printStackTrace);
    }

    <T> SensorNode<T> getSensorNode(Sensor<T> sensor) {
        return (SensorNode<T>) sensorNodes.computeIfAbsent(sensor.key(), (mapKey) -> new SensorNode<T>(sensor.key()));
    }

    private ConcurrentMap<Key, SensorNode<?>> sensorNodes = new ConcurrentHashMap<>();
}
