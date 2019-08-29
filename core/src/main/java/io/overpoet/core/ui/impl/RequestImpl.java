package io.overpoet.core.ui.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.overpoet.core.ui.Request;

public class RequestImpl implements Request {

    public RequestImpl(PathMatch match, FullHttpRequest request) {
        this.request = request;
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        this.params.putAll( decoder.parameters() );
        this.params.putAll( match.getAll() );
    }

    @Override
    public String uri() {
        return this.request.uri();
    }

    @Override
    public List<String> parameters(String name) {
        return this.params.get(name);
    }

    @Override
    public String header(String name) {
        return this.request.headers().get(name);
    }

    @Override
    public ByteBuf content() {
        return this.request.content();
    }

    private final FullHttpRequest request;
    private final Map<String, List<String>> params = new HashMap<>();
}
