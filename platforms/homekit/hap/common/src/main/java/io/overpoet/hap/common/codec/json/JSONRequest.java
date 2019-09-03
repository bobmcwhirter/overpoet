package io.overpoet.hap.common.codec.json;

import java.io.IOException;

import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.spi.JsonProvider;

import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.http.FullHttpRequest;

public class JSONRequest {

    public JSONRequest(FullHttpRequest request, JsonStructure content) throws IOException {
        this.httpRequest = request;
        this.content = content;

    }

    public FullHttpRequest httpRequest() {
        return this.httpRequest;
    }

    public JsonStructure content() {
        return this.content;
    }

    public JsonObject objectContent() {
        return (JsonObject) this.content;
    }

    private final FullHttpRequest httpRequest;
    private final JsonStructure content;
}
