package com.overpoet.core.engine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.overpoet.core.platform.Platform;

public interface ConfigurationProvider {
    Properties forPlatform(Platform platform);
}
