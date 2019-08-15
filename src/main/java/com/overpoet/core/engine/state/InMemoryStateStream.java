package com.overpoet.core.engine.state;

public class InMemoryStateStream implements StateStream {

    private InMemoryState currentHead = InMemoryState.NIL;

    public InMemoryStateStream() {

    }

    @Override
    public InMemoryState currentHead() {
        return this.currentHead;
    }

    public synchronized <T> InMemoryStateStream add(Sense<T> sense) throws StateException {
        this.currentHead = this.currentHead.add(sense);
        return this;
    }

    public synchronized <T> InMemoryStateStream add(Actuation<T> actuation) throws StateException {
        this.currentHead = this.currentHead.add(actuation);
        return this;
    }
}
