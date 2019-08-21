package io.overpoet.core.platform;

import java.util.Set;

public interface PlatformConfiguration {
    void set(String key, String value);
    String get(String key);
    Set<String> keys();
}
