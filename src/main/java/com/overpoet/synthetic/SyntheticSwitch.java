package com.overpoet.synthetic;

import com.overpoet.core.actuator.Switch;
import com.overpoet.core.sensor.SenseException;

public class SyntheticSwitch implements Switch {

    public SyntheticSwitch(String id) {
        this.id = id;
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public void turnOff() {
        try {
            this.sink.sink(false);
        } catch (SenseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void turnOn() {
        try {
            this.sink.sink(true);
        } catch (SenseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class<Boolean> datatype() {
        return Boolean.class;
    }

    @Override
    public void initialize(Sink<Boolean> sink) {
        this.sink = sink;
    }

    private final String id;

    private Sink<Boolean> sink;
}
