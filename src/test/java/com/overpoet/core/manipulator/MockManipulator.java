package com.overpoet.core.manipulator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MockManipulator implements Manipulator {

    public <T> void sensorHandler(String sensorId, Consumer<T> handler) {
        this.sensorHandlers.put(sensorId, handler);
    }

    @Override
    public void register(Apparatus apparatus) {
        for (Sensor<?> sensor : apparatus.sensors()) {
            Consumer handler = sensorHandlers.get(sensor.id());
            if ( handler != null ) {
                sensor.onChange(handler);
            }
        }
    }

    private Map<String, Consumer<?>> sensorHandlers = new HashMap<>();
}
