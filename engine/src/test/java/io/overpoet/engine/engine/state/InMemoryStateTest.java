package io.overpoet.engine.engine.state;

import io.overpoet.spi.TypedKey;
import io.overpoet.spi.metadata.StringMetadata;
import org.junit.Test;

import static io.overpoet.spi.TypedKey.keyOf;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

public class InMemoryStateTest {

    @Test
    public void moveFromNil() throws Exception {
        TypedKey<String> sensor = keyOf(String.class, "sensor-1");
        InMemoryState next = InMemoryState.NIL.add(new Sense<>(sensor, "Hello"));
        assertThat(next.value(sensor)).isEqualTo("Hello");
    }

    @Test
    public void multipleMoves() throws Exception {
        TypedKey<String> sensor = keyOf(String.class, "sensor-1");
        InMemoryState s1 = InMemoryState.NIL.add(new Sense<>(sensor, "Hello"));
        InMemoryState s2 = s1.add(new Sense<>(sensor, "Howdy"));
        InMemoryState s3 = s2.add(new Sense<>(sensor, "Bonjour"));

        assertThat(s1.value(sensor)).isEqualTo("Hello");
        assertThat(s2.value(sensor)).isEqualTo("Howdy");
        assertThat(s3.value(sensor)).isEqualTo("Bonjour");
    }

    @Test
    public void complexChanges() throws Exception {
        TypedKey<String> sensor1 = keyOf(String.class, "sensor-1");
        TypedKey<String> sensor2 = keyOf(String.class, "sensor-1");

        InMemoryState s1 = InMemoryState.NIL.add(new Sense<>(sensor1, "1-Howdy"));
        InMemoryState s2 = s1.add(new Sense<>(sensor2, "2-Howdy"));
        InMemoryState s3 = s2.add(new Sense<>(sensor1, "1-Hello"));

        assertThat(s3.value(sensor1)).isEqualTo("1-Hello");
        assertThat(s3.value(sensor2)).isEqualTo("2-Howdy");
    }

    @Test
    public void disallowBranching() throws Exception {
        TypedKey<String> sensor = keyOf(String.class, "sensor-1");

        InMemoryState s1 = InMemoryState.NIL.add(new Sense<>(sensor, "Howdy"));
        InMemoryState s2 = s1.add(new Sense<>(sensor, "Hello"));
        try {
            s2 = s1.add(new Sense<>(sensor, "Bonjour"));
            fail("should have thrown exception");
        } catch (StateException e) {
            // expected and correct.
        }

    }
}
