package io.overpoet.lutron.leap.client.protocol;

public class GetZones extends AbstractRequestMessage {
    public GetZones() {
        url("/zone");
        communiqueType("ReadRequest");
    }

}
