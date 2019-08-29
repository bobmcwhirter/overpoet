package io.overpoet.core.platform;

import java.util.concurrent.ScheduledExecutorService;

import io.overpoet.core.apparatus.Apparatus;
import io.overpoet.core.manipulator.Manipulator;
import io.overpoet.core.ui.UI;

public interface PlatformContext {
    void connect(Apparatus apparatus);
    void connect(Manipulator manipulator);

    PlatformConfiguration configuration();
    ScheduledExecutorService executor();
    UI ui();
}

