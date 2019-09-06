package io.overpoet.lutron.leap.client.protocol;

public class GetZonesStatus extends AbstractRequestMessage {
    public GetZonesStatus() {
        url("/zone/status");
        communiqueType("ReadRequest");
    }

}
