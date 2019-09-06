package io.overpoet.lutron.caseta.bridge.protocol;

public class GetDevices extends AbstractRequestMessage {
    public GetDevices() {
        url("/device");
        communiqueType("ReadRequest");
    }

}
