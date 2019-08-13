package com.overpoet.core.rule;

import com.overpoet.Key;
import com.overpoet.core.manipulator.Manipulator;

public class RuleEngine implements Manipulator {

    public RuleEngine() {

    }

    @Override
    public void register(Apparatus apparatus) {
        for (Sensor<?> sensor : apparatus.sensors()) {
            sensor.onChange( (value)->{
                onChange( sensor.key(), value);
            });
        }
    }

    private void onChange(Key sensor, Object value) {
        //root.assertFact(sensor, value);
    }

    private final RootNode rootNode = new RootNode();

}
