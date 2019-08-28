package io.overpoet.hap.common.codec.json;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class JSONResponse {

    public JSONResponse(HttpVersion httpVersion, HttpResponseStatus status, JsonStructure content) {
        this.httpVersion = httpVersion;
        this.status = status;
        this.content = content;
    }

    public HttpVersion httpVersion() {
        return this.httpVersion;
    }

    public HttpResponseStatus status() {
        return this.status;
    }

    public JsonStructure content() {
        return this.content;
    }

    private final HttpVersion httpVersion;

    private final HttpResponseStatus status;

    private final JsonStructure content;
}
