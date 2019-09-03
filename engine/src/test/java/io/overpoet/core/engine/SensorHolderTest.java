package io.overpoet.core.engine;

import java.util.concurrent.atomic.AtomicReference;

import io.overpoet.core.engine.state.InMemoryStateStream;
import io.overpoet.spi.manipulator.Manipulator;
import io.overpoet.spi.sensor.BaseSensorLogic;
import io.overpoet.spi.metadata.StringMetadata;
import io.overpoet.spi.sensor.StringSensor;
import org.junit.Test;

import static io.overpoet.spi.Key.keyOf;
import static org.fest.assertions.api.Assertions.assertThat;

public class SensorHolderTest {

    @Test
    public void wiringToState() throws Exception {
        InMemoryStateStream state = new InMemoryStateStream();
        BaseSensorLogic<String> logic = new BaseSensorLogic<>();
        StringSensor sensor = new StringSensor(keyOf("sensor-1"), new StringMetadata(), logic);
        SensorHolder<String> holder = new SensorHolder<>(state, sensor);

        logic.sink("howdy");
        assertThat(state.currentHead().value(sensor)).isEqualTo("howdy");

        logic.sink("hello");
        assertThat(state.currentHead().value(sensor)).isEqualTo("hello");
    }

    @Test
    public void wiringToManipulators() throws Exception {
        InMemoryStateStream state = new InMemoryStateStream();
        BaseSensorLogic<String> logic = new BaseSensorLogic<>();
        StringSensor sensor = new StringSensor(keyOf("sensor-1"), new StringMetadata(), logic);
        SensorHolder<String> holder = new SensorHolder<>(state, sensor);

        Manipulator manipulator1 = (app)->{};
        Manipulator manipulator2 = (app)->{};
        Manipulator manipulator3 = (app)->{};

        StringSensor msensor1 = (StringSensor) holder.forManipulator(manipulator1);
        StringSensor msensor2 = (StringSensor) holder.forManipulator(manipulator2);
        StringSensor msensor3 = (StringSensor) holder.forManipulator(manipulator2);

        assertThat(msensor1.key()).isEqualTo(keyOf("sensor-1"));
        assertThat(msensor2.key()).isEqualTo(keyOf("sensor-1"));
        assertThat(msensor3.key()).isEqualTo(keyOf("sensor-1"));

        AtomicReference ref1 = new AtomicReference();
        AtomicReference ref2 = new AtomicReference();

        msensor1.onChange( (v)->{
            ref1.set("manipulator1:" + v);
        });

        msensor2.onChange( (v)->{
            ref2.set("manipulator2:" + v);
        });

        logic.sink("howdy");

        assertThat(ref1.get()).isEqualTo("manipulator1:howdy");
        assertThat(ref2.get()).isEqualTo("manipulator2:howdy");
    }
}
