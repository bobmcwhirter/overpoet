package io.overpoet.spi.apparatus;

import java.util.HashSet;
import java.util.Set;

import io.overpoet.spi.Key;
import io.overpoet.spi.actuator.Actuator;
import io.overpoet.spi.aspect.Aspect;
import io.overpoet.spi.metadata.ApparatusMetadata;
import io.overpoet.spi.sensor.Sensor;

public class SimpleApparatus implements Apparatus {

    public SimpleApparatus(ApparatusMetadata metadata, Key key, Set<Aspect<?, ?>> aspects) {
        this.metadata = metadata;
        this.key = key;
        this.aspects.addAll(aspects);
    }

    public SimpleApparatus(ApparatusMetadata metadata, Key key, Aspect<?, ?>... aspects) {
        this.metadata = metadata;
        this.key = key;
        for (Aspect<?, ?> aspect : aspects) {
            this.aspects.add(aspect);
        }
    }

    @Override
    public ApparatusMetadata metadata() {
        return this.metadata;
    }

    @Override
    public Set<Aspect<?, ?>> aspects() {
        return this.aspects;
    }

    public Key key() {
        return this.key;
    }

    private final ApparatusMetadata metadata;

    private final Key key;

    final private Set<Aspect<?, ?>> aspects = new HashSet<>();
}
