package io.overpoet.core.metadata;

import io.overpoet.core.measurement.Bearing;

public class BearingMetadata implements Metadata<Bearing> {

    public static final BearingMetadata DEFAULT = new BearingMetadata(Bearing.MIN, Bearing.MAX);

    public BearingMetadata(Bearing minimum, Bearing maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public Bearing minimum() {
        return this.minimum;
    }

    public Bearing maximum() {
        return this.maximum;
    }

    private final Bearing minimum;
    private final Bearing maximum;
}
