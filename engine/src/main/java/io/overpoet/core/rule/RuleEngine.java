package io.overpoet.core.rule;

import io.overpoet.core.apparatus.Apparatus;
import io.overpoet.core.manipulator.Manipulator;
import io.overpoet.core.sensor.Sensor;
import io.overpoet.core.geo.Location;

public class RuleEngine implements Manipulator {

    public RuleEngine(Location location) {
        this.location = location;
    }

    public Location location() {
        return this.location;
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

    private Location location;
    private final RootNode rootNode = new RootNode();
}
