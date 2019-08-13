package com.overpoet.core.engine.state;

import com.overpoet.core.sensor.SimpleSensor;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

public class StateTest {

    @Test
    public void moveFromNil() throws Exception {
        SimpleSensor<String> sensor = new SimpleSensor<>("sensor-1", String.class);
        State next = State.NIL.add(new SimpleSense<>(sensor, "Hello"));
        assertThat(next.value(sensor)).isEqualTo("Hello");
    }

    @Test
    public void multipleMoves() throws Exception {
        SimpleSensor<String> sensor = new SimpleSensor<>("sensor-1", String.class);
        State s1 = State.NIL.add(new SimpleSense<>(sensor, "Hello"));
        State s2 = s1.add(new SimpleSense<>(sensor, "Howdy"));
        State s3 = s2.add(new SimpleSense<>(sensor, "Bonjour"));

        assertThat(s1.value(sensor)).isEqualTo("Hello");
        assertThat(s2.value(sensor)).isEqualTo("Howdy");
        assertThat(s3.value(sensor)).isEqualTo("Bonjour");
    }

    @Test
    public void complexChanges() throws Exception {
        SimpleSensor<String> sensor1 = new SimpleSensor<>("sensor-1", String.class);
        SimpleSensor<String> sensor2 = new SimpleSensor<>("sensor-2", String.class);

        State s1 = State.NIL.add(new SimpleSense<>(sensor1, "1-Howdy"));
        State s2 = s1.add(new SimpleSense<>(sensor2, "2-Howdy"));
        State s3 = s2.add(new SimpleSense<>(sensor1, "1-Hello"));

        assertThat(s3.value(sensor1)).isEqualTo("1-Hello");
        assertThat(s3.value(sensor2)).isEqualTo("2-Howdy");
    }

    @Test
    public void disallowBranching() throws Exception {
        SimpleSensor<String> sensor = new SimpleSensor<>("sensor-1", String.class);

        State s1 = State.NIL.add(new SimpleSense<>(sensor, "Howdy"));
        State s2 = s1.add(new SimpleSense<>(sensor, "Hello"));
        try {
            s2 = s1.add(new SimpleSense<>(sensor, "Bonjour"));
            fail("should have thrown exception");
        } catch (StateException e) {
            // expected and correct.
        }

    }
}
