package com.overpoet.core.rule;

import com.overpoet.Key;
import com.overpoet.core.manipulator.Manipulator;
import com.overpoet.core.sensor.Sensor;

public class RuleEngine implements Manipulator {

    public RuleEngine() {

    }

    public void addRule(Rule rule) {
        rule.build(this.rootNode);
    }

    @Override
    public void register(Apparatus apparatus) {
        for (Sensor<?> sensor : apparatus.sensors()) {
            sensor.onChange( (value)->{
                //onChange( sensor.key(), value);
            });
        }
    }

    public <T> void assertSensor(Sensor<T> sensor, T value) {
        this.rootNode.assertSensor(sensor, value);
    }

    private void onChange(Key sensor, Object value) {
        //root.assertFact(sensor, value);
    }

    private final RootNode rootNode = new RootNode();

}
