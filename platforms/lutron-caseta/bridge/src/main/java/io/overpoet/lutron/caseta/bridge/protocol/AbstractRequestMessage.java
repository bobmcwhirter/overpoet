package io.overpoet.lutron.caseta.bridge.protocol;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

public abstract class AbstractRequestMessage {

    protected AbstractRequestMessage() {
        this.body = JsonProvider.provider().createObjectBuilder();
        this.header = JsonProvider.provider().createObjectBuilder();

    }

    protected void header(String name, String value) {
        this.header.add(name, value);
    }

    protected void body(String name, String value)  {
        this.body.add(name, value);
    }


    protected void url(String url) {
        header("Url", url);
    }

    protected void communiqueType(String type) {
        body( "CommuniqueType", type);
    }

    JsonObjectBuilder body() {
        return this.body;
    }

    JsonObjectBuilder header() {
        return header;
    }

    public JsonObjectBuilder toJSON() {
        this.body.add("Header", header);
        return this.body;
    }

    private JsonObjectBuilder body;
    private JsonObjectBuilder header;

}
