package com.overpoet.core.rule;

import com.overpoet.core.apparatus.Apparatus;
import com.overpoet.core.manipulator.Manipulator;
import com.overpoet.core.sensor.Sensor;

public class RuleEngine implements Manipulator {

    public RuleEngine() {

    }

    public void addRule(Rule rule) {
        rule.build(this.rootNode);
    }

    @Override
    public void connect(Apparatus apparatus) {
        for (Sensor<?> sensor : apparatus.sensors()) {
            register(sensor);
        }
    }

    public <T> void register(Sensor<T> sensor) {
        sensor.onChange( (value)->{
            onChange(sensor, value);
        });
    }

    public <T> void assertSensor(Sensor<T> sensor, T value) {
        this.rootNode.assertSensor(sensor, value);
    }

    private <T> void onChange(Sensor<T> sensor, T value) {
        this.rootNode.assertSensor(sensor, value);
    }

    private final RootNode rootNode = new RootNode();

}
