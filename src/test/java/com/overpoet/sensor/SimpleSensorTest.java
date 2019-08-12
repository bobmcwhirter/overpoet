package com.overpoet.sensor;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

public class SimpleSensorTest {

    @Test
    public void testSinkBeforeInitialize() throws Exception {
        SimpleSensor<String> sensor = new SimpleSensor<>("sensor-1", String.class);
        assertThat(sensor.datatype()).isEqualTo(String.class);
        try {
            sensor.sink("hello");
            fail("should have thrown exception");
        } catch (SensorInitializationException e) {
            // expected and correct
        }
    }

    @Test
    public void testSinkAfterInitialize() throws Exception {
        SimpleSensor<String> sensor = new SimpleSensor<>("sensor-1", String.class);
        assertThat(sensor.datatype()).isEqualTo(String.class);

        List<String> values = new ArrayList<>();

        sensor.initialize( (v)->{
            values.add( v );
        });

        sensor.sink("hello");

        assertThat( values ).contains("hello");
    }
}
