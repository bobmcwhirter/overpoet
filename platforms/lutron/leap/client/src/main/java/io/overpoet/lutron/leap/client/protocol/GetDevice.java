package io.overpoet.lutron.leap.client.protocol;

public class GetDevice extends AbstractRequestMessage {
    public GetDevice(String href) {
        url(href);
        communiqueType("ReadRequest");
    }

}
