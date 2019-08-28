package io.overpoet.hap.common.codec.json;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.JsonWriter;
import javax.json.spi.JsonProvider;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;

public class JSONResponseEncoder extends MessageToMessageEncoder<JSONResponse> {

    @Override
    protected void encode(ChannelHandlerContext ctx, JSONResponse msg, List<Object> out) throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        try ( ByteBufOutputStream byteStream = new ByteBufOutputStream(buf) ) {
            Map<String, Object> config = new HashMap<>();
            JsonWriter writer = JsonProvider.provider().createWriterFactory(config).createWriter(byteStream);
            writer.write( msg.content() );
        }

        DefaultFullHttpResponse response = new DefaultFullHttpResponse(msg.httpVersion(), msg.status(), buf);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/hap+json");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
        System.err.println( "encoded JSON to: " + response);
        System.err.println( "[" + buf.toString(Charset.forName("UTF-8")) + "]");
        out.add(response);
    }
}
