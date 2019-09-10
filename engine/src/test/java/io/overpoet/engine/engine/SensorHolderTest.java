package io.overpoet.engine.engine;

import java.util.concurrent.atomic.AtomicReference;

import io.overpoet.engine.engine.state.InMemoryStateStream;
import io.overpoet.spi.Key;
import io.overpoet.spi.manipulator.Manipulator;
import io.overpoet.spi.sensor.BaseSensor;
import io.overpoet.spi.metadata.StringMetadata;
import io.overpoet.spi.sensor.Sensor;
import org.junit.Test;

import static io.overpoet.spi.Key.keyOf;
import static org.fest.assertions.api.Assertions.assertThat;

public class SensorHolderTest {

    @Test
    public void wiringToState() throws Exception {
        InMemoryStateStream state = new InMemoryStateStream();
        Key key = keyOf("sensor-1");
        BaseSensor<String> sensor = new BaseSensor<>();
        SensorHolder<String> holder = new SensorHolder<>(state, key, sensor);

        sensor.sink("howdy");
        assertThat(state.currentHead().value(key)).isEqualTo("howdy");

        sensor.sink("hello");
        assertThat(state.currentHead().value(key)).isEqualTo("hello");
    }

    @Test
    public void wiringToManipulators() throws Exception {
        InMemoryStateStream state = new InMemoryStateStream();
        Key key = keyOf("sensor-1");
        BaseSensor<String> sensor = new BaseSensor<String>();
        SensorHolder<String> holder = new SensorHolder<>(state, key, sensor);

        Manipulator manipulator1 = (app)->{};
        Manipulator manipulator2 = (app)->{};
        Manipulator manipulator3 = (app)->{};

        Sensor<String> msensor1 = holder.forManipulator(manipulator1);
        Sensor<String> msensor2 = holder.forManipulator(manipulator2);
        Sensor<String> msensor3 = holder.forManipulator(manipulator2);

        AtomicReference ref1 = new AtomicReference();
        AtomicReference ref2 = new AtomicReference();

        msensor1.onChange( (v)->{
            ref1.set("manipulator1:" + v);
        });

        msensor2.onChange( (v)->{
            ref2.set("manipulator2:" + v);
        });

        sensor.sink("howdy");

        assertThat(ref1.get()).isEqualTo("manipulator1:howdy");
        assertThat(ref2.get()).isEqualTo("manipulator2:howdy");
    }
}
