package io.overpoet.core.ui.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathMatch {
    public void set(String key, String value) {
        this.values.put( key, value );
    }

    public String get(String key) {
        return this.values.get(key);
    }

    public Map<String, List<String>> getAll() {
        Map<String,List<String>> all = new HashMap<>();
        this.values.entrySet().forEach(e->{
            all.put(e.getKey(), Collections.singletonList(e.getValue()));
        });
        return all;
    }

    private Map<String, String> values = new HashMap<>();
}
