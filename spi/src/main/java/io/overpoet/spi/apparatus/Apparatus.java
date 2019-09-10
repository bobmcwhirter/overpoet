package io.overpoet.spi.apparatus;

import java.util.Set;

import io.overpoet.spi.Keyed;
import io.overpoet.spi.aspect.Aspect;
import io.overpoet.spi.metadata.ApparatusMetadata;

public interface Apparatus extends Keyed {
    ApparatusMetadata metadata();
    Set<Aspect<?,?>> aspects();
}
