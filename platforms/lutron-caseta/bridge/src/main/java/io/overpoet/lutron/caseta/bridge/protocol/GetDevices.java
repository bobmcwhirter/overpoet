package io.overpoet.lutron.caseta.bridge.protocol;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

public class GetDevices extends AbstractRequestMessage {
    public GetDevices() {

    }

    @Override
    public JsonObject toJSON() {
        JsonObjectBuilder builder = JsonProvider.provider().createObjectBuilder();

        builder.add( "CommuniqueType", "ReadRequest");
        //builder.add( "CommuniqueType", "SubscribeRequest");

        JsonObjectBuilder header = JsonProvider.provider().createObjectBuilder();
        header.add("Url", "/device");
        builder.add( "Header", header );

        return builder.build();
    }
}
