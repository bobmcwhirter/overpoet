package io.overpoet.core.platform;

public interface PlatformConfiguration {
    void set(String key, String value);
    String get(String key);
}
