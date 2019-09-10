package io.overpoet.spi.sensor;

public class BaseSensor<T> implements Sensor<T>, Sink<T> {

    public void sink(T value) {
        this.lastValue = value;
        System.err.println( this + " setLastValue: " + this.lastValue);
        if ( this.sink != null ) {
            this.sink.sink(value);
        }
    }

    @Override
    public void onChange(Sink<T> sink) {
        this.sink = sink;
        if ( this.sink != null & this.lastValue != null ) {
            this.sink.sink(this.lastValue);
        }
    }

    private Sink<T> sink;

    private T lastValue;
}
