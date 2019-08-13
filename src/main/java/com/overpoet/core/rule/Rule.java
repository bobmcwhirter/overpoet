package com.overpoet.core.rule;

import java.util.concurrent.Callable;

import com.overpoet.Key;
import com.overpoet.core.AbstractIdentified;

public class Rule extends AbstractIdentified {

    interface Sensor<T> {
        Class<T> datatype();
        Key key();
    }

    public Rule(String id) {
        super(id);
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

    private Condition when;
    private Action then;

}
