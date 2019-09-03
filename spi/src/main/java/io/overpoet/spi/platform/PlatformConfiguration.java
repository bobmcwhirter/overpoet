package io.overpoet.spi.platform;

import java.util.Set;

public interface PlatformConfiguration {
    void set(String key, String value);
    String get(String key);
    void remove(String ltpk);
    Set<String> keys();
}
