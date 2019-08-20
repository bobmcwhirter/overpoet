package com.overpoet.core.engine.state;

public class InMemoryStateStream implements StateStream {

    private InMemoryState currentHead = InMemoryState.NIL;

    public InMemoryStateStream() {

    }

    @Override
    public InMemoryState currentHead() {
        return this.currentHead;
    }

    public synchronized <T> boolean add(Sense<T> sense) throws StateException {
        InMemoryState newHead = this.currentHead.add(sense);
        if ( newHead == this.currentHead ) {
            return false;
        }
        this.currentHead = newHead;
        return true;
    }

    public synchronized <T> boolean add(Actuation<T> actuation) throws StateException {
        InMemoryState newHead = this.currentHead.add(actuation);
        if ( newHead == this.currentHead ) {
            return false;
        }
        this.currentHead = newHead;
        return true;
    }
}
