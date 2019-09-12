package io.overpoet.lutron.leap.client.protocol;

public class Probe extends AbstractRequestMessage {
    public Probe() {
        url("/zone");
        communiqueType("");
    }

}
