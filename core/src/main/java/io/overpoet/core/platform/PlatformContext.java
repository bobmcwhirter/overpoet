package io.overpoet.core.platform;

import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;

import io.overpoet.core.apparatus.Apparatus;
import io.overpoet.core.manipulator.Manipulator;

public interface PlatformContext {
    void connect(Apparatus apparatus);
    void connect(Manipulator manipulator);

    PlatformConfiguration configuration();
    ScheduledExecutorService executor();
}

