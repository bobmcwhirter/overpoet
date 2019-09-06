package io.overpoet.lutron.caseta.bridge.protocol;

public class GetAreas extends AbstractRequestMessage {
    public GetAreas() {
        url("/area");
        communiqueType("ReadRequest");
    }

}
