package com.overpoet.core.engine;

import com.overpoet.Key;
import com.overpoet.core.engine.state.StateStream;
import com.overpoet.core.sensor.MockSensor;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class SensorHolderTest {

    @Test
    public void wiringToState() throws Exception {
        StateStream state = new StateStream();
        MockSensor<String> sensor = new MockSensor<>("sensor-1", String.class);
        SensorHolder<String> holder = new SensorHolder<>(state, Key.of("sensor-1"), sensor);

        sensor.sink("howdy");
        assertThat(state.currentHead().value(sensor)).isEqualTo("howdy");

        sensor.sink("hello");
        assertThat(state.currentHead().value(sensor)).isEqualTo("hello");
    }
}
