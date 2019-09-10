package io.overpoet.automation.rule;

import io.overpoet.spi.TypedKey;
import io.overpoet.spi.apparatus.Apparatus;
import io.overpoet.spi.manipulator.Manipulator;
import io.overpoet.spi.geo.Location;

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
        /*
        for (Sensor<?> sensor : apparatus.sensors()) {
            register(sensor);
        }
         */
    }

    /*
    public <T> void register(Sensor<T> sensor) {
        sensor.onChange( (value)->{
            onChange(sensor, value);
        });
    }
     */

    public <T> void assertSensor(TypedKey<T> key, T value) {
        this.rootNode.assertSensor(key, value);
    }

    /*
    private <T> void onChange(Sensor<T> sensor, T value) {
        this.rootNode.assertSensor(sensor, value);
    }
     */

    private Location location;
    private final RootNode rootNode = new RootNode();
}
