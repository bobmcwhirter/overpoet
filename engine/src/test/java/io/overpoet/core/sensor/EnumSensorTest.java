package io.overpoet.core.sensor;

import java.util.concurrent.atomic.AtomicReference;

import io.overpoet.core.metadata.EnumMetadata;
import org.junit.Test;

import static io.overpoet.Key.keyOf;
import static io.overpoet.core.sensor.EnumSensorTest.State.LOCKED;
import static io.overpoet.core.sensor.EnumSensorTest.State.UNLOCKED;
import static org.fest.assertions.api.Assertions.assertThat;


public class EnumSensorTest {

    public enum State {
        LOCKED,
        UNLOCKED
    }

    @Test
    public void testEnumSensorAndMetadata() {

        EnumMetadata<State> meta = new EnumMetadata<>(State.class);
        State[] values = meta.possibleValues();

        assertThat(values[0]).isSameAs(LOCKED);
        assertThat(values[1]).isSameAs(UNLOCKED);

        BaseSensorLogic<State> logic = new BaseSensorLogic<>();
        EnumSensor<State> sensor = new EnumSensor<>(keyOf("sensor-1"), State.class, meta, logic);

        AtomicReference<State> ref = new AtomicReference<>(UNLOCKED);

        sensor.onChange(ref::set);

        logic.sink(LOCKED);

        assertThat(ref.get()).isEqualTo(LOCKED);

    }
}
