package io.overpoet.spi.metadata;

import io.overpoet.spi.apparatus.ApparatusType;

public class SimpleApparatusMetadata implements ApparatusMetadata {

    public SimpleApparatusMetadata(ApparatusType type,
                                   String name,
                                   String manufacturer,
                                   String model,
                                   String version,
                                   String serialNumber) {
        this.type = type;
        this.name = name;
        this.manufacturer = manufacturer;
        this.model = model;
        this.version = version;
        this.serialNumber = serialNumber;
    }

    @Override
    public ApparatusType type() {
        return this.type;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String manufacturer() {
        return this.manufacturer;
    }

    @Override
    public String model() {
        return this.model;
    }

    @Override
    public String version() {
        return this.version;
    }

    @Override
    public String serialNumber() {
        return this.serialNumber;
    }

    private final ApparatusType type;

    private final String name;

    private final String manufacturer;

    private final String model;

    private final String version;

    private final String serialNumber;
}
