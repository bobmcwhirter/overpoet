package com.overpoet.core.platform;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.overpoet.core.apparatus.Apparatus;
import com.overpoet.core.manipulator.Manipulator;

public interface PlatformContext {
    void connect(Apparatus apparatus);
    void connect(Manipulator manipulator);

    Properties configuration();
}

