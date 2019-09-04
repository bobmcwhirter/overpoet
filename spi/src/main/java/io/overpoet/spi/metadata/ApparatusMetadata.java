package io.overpoet.spi.metadata;

import io.overpoet.spi.apparatus.ApparatusType;

public interface ApparatusMetadata {
    ApparatusType type();
    String name();
    String manufacturer();
    String model();
    String version();
    String serialNumber();
}
