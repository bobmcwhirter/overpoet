package io.overpoet.lutron.caseta.bridge.protocol;

public class GetZones extends AbstractRequestMessage {
    public GetZones() {
        url("/zone");
        communiqueType("ReadRequest");
    }

}
