package io.overpoet.lutron.leap.client.protocol;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

public abstract class AbstractRequestMessage {

    protected AbstractRequestMessage() {
        this.payload = JsonProvider.provider().createObjectBuilder();
        this.body = JsonProvider.provider().createObjectBuilder();
        this.header = JsonProvider.provider().createObjectBuilder();

    }

    protected void header(String name, String value) {
        this.header.add(name, value);
    }

    protected void body(String name, String value)  {
        this.body.add(name, value);
    }

    protected void body(String name, JsonObject value) {
        this.body.add(name, value);
    }

    protected void url(String url) {
        header("Url", url);
    }

    protected void communiqueType(String type) {
        this.payload.add( "CommuniqueType", type);
    }

    JsonObjectBuilder body() {
        return this.body;
    }

    JsonObjectBuilder header() {
        return header;
    }

    public JsonObjectBuilder toJSON() {
        this.payload.add("Header", this.header);
        JsonObject bodyJson = this.body.build();
        if (!bodyJson.keySet().isEmpty()) {
            payload.add("Body", bodyJson);
        }

        return payload;
    }

    private JsonObjectBuilder payload;
    private JsonObjectBuilder body;
    private JsonObjectBuilder header;

}
