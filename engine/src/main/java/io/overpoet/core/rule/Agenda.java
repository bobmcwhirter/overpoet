package io.overpoet.core.rule;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Agenda {

    void addAction(Action action) {
        this.actions.add( action );
    }

    void removeAction(Action action) {
        this.actions.remove( action );
    }

    void process(Consumer<Exception> exceptionHandler) {
        for (Action action : actions) {
            try {
                action.run();
            } catch (Exception e) {
                exceptionHandler.accept(e);
            }
        }
        this.actions.clear();
    }

    private final Set<Action> actions = new HashSet<>();
}
