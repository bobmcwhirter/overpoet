package io.overpoet.spi.platform;

import java.util.concurrent.ScheduledExecutorService;

import io.overpoet.spi.apparatus.Apparatus;
import io.overpoet.spi.manipulator.Manipulator;
import io.overpoet.spi.ui.UI;

public interface PlatformContext {
    void connect(Apparatus apparatus);
    void connect(Manipulator manipulator);

    PlatformConfiguration configuration();
    ScheduledExecutorService executor();
    UI ui();
}

