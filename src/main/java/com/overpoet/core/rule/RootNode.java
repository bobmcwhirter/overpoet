package com.overpoet.core.rule;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.overpoet.Key;
import com.overpoet.core.sensor.Sensor;

class RootNode {

    RootNode() {

    }

    <T> void assertSensor(Sensor<T> sensor, T value) {
        Agenda agenda = new Agenda();
        getSensorNode(sensor).assertValue(agenda, value);
        agenda.process(Throwable::printStackTrace);
    }

    <T> SensorNode<T> getSensorNode(Sensor<T> sensor) {
        return (SensorNode<T>) inputNodes.computeIfAbsent(sensor.key(), (mapKey) -> new SensorNode<T>(sensor.key()));
    }

            /*
    @SuppressWarnings("unchecked")
    <T> SensorNode<T> getSensorNode(Key key) {
        return (SensorNode<T>) inputNodes.computeIfAbsent(key, (mapKey) -> new SensorNode<T>(key));
    }
             */



    private ConcurrentMap<Key, SensorNode<?>> inputNodes = new ConcurrentHashMap<>();

}
