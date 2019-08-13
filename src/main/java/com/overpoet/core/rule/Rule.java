package com.overpoet.core.rule;

import java.util.concurrent.Callable;

import com.overpoet.Identified;
import com.overpoet.Key;
import com.overpoet.core.AbstractIdentified;

public class Rule implements Identified {

    interface Sensor<T> {
        Class<T> datatype();
        Key key();
    }

    public Rule(String id) {
        this.id = id;
    }

    @Override
    public String id() {
        return this.id;
    }

    Rule when(Callable<Condition> condition) {
        try {
            this.when = condition.call();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;

    }

    void then(Action then) {
        this.then = then;
    }

    private final String id;
    private Condition when;
    private Action then;

}
