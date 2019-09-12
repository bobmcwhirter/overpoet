package io.overpoet.homekit.manipulator;

import java.util.concurrent.atomic.AtomicInteger;

import io.overpoet.hap.server.model.ServerAccessory;
import io.overpoet.spi.apparatus.Apparatus;

public interface ApparatusHandler {

    void handle(AtomicInteger iid, ServerAccessory a, Apparatus apparatus);
}
