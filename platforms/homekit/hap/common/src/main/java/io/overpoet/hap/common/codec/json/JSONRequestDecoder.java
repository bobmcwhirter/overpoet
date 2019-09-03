package io.overpoet.hap.common.codec.json;

import java.util.List;

import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.spi.JsonProvider;

import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;

public class JSONRequestDecoder extends MessageToMessageDecoder<FullHttpRequest> {

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
        String contentType = msg.headers().get(HttpHeaderNames.CONTENT_TYPE);
        if ( contentType == null || ! contentType.equals("application/hap+json")) {
            out.add( msg.retain() );
            return;
        }

        try (ByteBufInputStream in = new ByteBufInputStream(msg.content())) {
            JsonReader reader = JsonProvider.provider().createReader(in);
            JsonStructure json = reader.read();
            JSONRequest request = new JSONRequest(msg, json);
            out.add(request);
        }
    }
}
