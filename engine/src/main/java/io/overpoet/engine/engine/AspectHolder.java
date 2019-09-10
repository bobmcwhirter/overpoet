package io.overpoet.engine.engine;

import io.overpoet.engine.engine.state.StateStream;
import io.overpoet.spi.aspect.Aspect;
import io.overpoet.spi.aspect.AspectBuilder;
import io.overpoet.spi.manipulator.Manipulator;
import io.overpoet.spi.metadata.Metadata;

class AspectHolder<T, MT extends Metadata<T>> {

    AspectHolder(StateStream state, Aspect<T, MT> aspect) {
        this.state = state;
        this.aspect = aspect;
        this.sensor = new SensorHolder<>(this.state, aspect.key(), aspect.sensor());
        this.actuator = new ActuatorHolder<>(this.state, aspect.key(), aspect.actuator());
    }

    Aspect<T, MT> forManipulator(Manipulator manipulator) {
        return AspectBuilder.of(aspect.key(), aspect.datatype(), aspect.metadata())
                .withSensor(this.sensor.forManipulator(manipulator))
                .withActuator(this.actuator.forManipulator(manipulator));
    }

    private final StateStream state;
    private final Aspect<T, MT> aspect;

    private final SensorHolder<T> sensor;
    private final ActuatorHolder<T> actuator;

}
