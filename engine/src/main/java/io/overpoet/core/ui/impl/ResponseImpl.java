package io.overpoet.core.ui.impl;

import java.util.function.Consumer;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.overpoet.core.ui.Response;

public class ResponseImpl implements Response {

    public ResponseImpl(ByteBuf content, Consumer<FullHttpResponse> sink) {
        this.content = content;
        this.sink = sink;
    }

    @Override
    public ByteBuf content() {
        return this.content;
    }

    @Override
    public void close() {
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status(), this.content);
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, this.content.readableBytes());
        sink.accept(httpResponse);
    }

    @Override
    public void status(int status) {
        this.status = status;
    }

    private HttpResponseStatus status() {
        return HttpResponseStatus.valueOf(this.status);
    }

    private final Consumer<FullHttpResponse> sink;
    private int status = 200;
    private ByteBuf content;

}
