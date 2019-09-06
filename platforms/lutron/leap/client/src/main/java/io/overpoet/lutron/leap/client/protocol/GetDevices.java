package io.overpoet.lutron.leap.client.protocol;

public class GetDevices extends AbstractRequestMessage {
    public GetDevices() {
        url("/device");
        communiqueType("ReadRequest");
    }

}
