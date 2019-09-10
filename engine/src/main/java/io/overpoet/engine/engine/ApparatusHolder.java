package io.overpoet.engine.engine;

import java.util.HashSet;
import java.util.Set;

import io.overpoet.engine.engine.state.InMemoryStateStream;
import io.overpoet.spi.apparatus.Apparatus;
import io.overpoet.spi.apparatus.SimpleApparatus;
import io.overpoet.spi.aspect.Aspect;
import io.overpoet.spi.manipulator.Manipulator;
import io.overpoet.spi.metadata.Metadata;

class ApparatusHolder {

    ApparatusHolder(InMemoryStateStream state, Apparatus apparatus) {
        this.state = state;
        this.apparatus = apparatus;

        for (Aspect<?, ?> aspect : apparatus.aspects()) {
            this.aspects.add(wrap(aspect));
        }

    }

    Apparatus forManipulator(Manipulator manipulator) {
        Set<Aspect<?,?>> delegates = new HashSet<>();
        for (AspectHolder<?, ?> aspect : this.aspects) {
            delegates.add(aspect.forManipulator(manipulator));
        }

        SimpleApparatus wrapped = new SimpleApparatus(apparatus.metadata(), apparatus.key(), delegates);
        return wrapped;
    }

    private <T,MT extends Metadata<T>> AspectHolder<T,MT> wrap(Aspect<T,MT> aspect) {
        return new AspectHolder<>(this.state, aspect);
    }

    private final InMemoryStateStream state;

    private final Apparatus apparatus;

    private final Set<AspectHolder<?,?>> aspects = new HashSet<>();

}
