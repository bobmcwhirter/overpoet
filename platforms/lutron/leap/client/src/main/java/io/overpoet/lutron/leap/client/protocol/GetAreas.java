package io.overpoet.lutron.leap.client.protocol;

public class GetAreas extends AbstractRequestMessage {
    public GetAreas() {
        url("/area");
        communiqueType("ReadRequest");
    }

}
