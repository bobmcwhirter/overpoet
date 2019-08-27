package io.overpoet.hap.server.codec;

import java.nio.charset.Charset;
import java.util.List;

import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.spi.JsonProvider;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.overpoet.hap.common.codec.tlv.TLV;

/**
 * Created by bob on 8/28/18.
 */
public class EventEncoder extends MessageToMessageEncoder<Event> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Event event, List<Object> out) throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        try (ByteBufOutputStream os = new ByteBufOutputStream(buf)) {
            JsonWriter writer = JsonProvider.provider().createWriter(os);
            writer.writeObject(event.toJSON().build());
            writer.close();
        }
        //buf.writeByte('\r');
        //buf.writeByte('\n');
        //buf.writeByte('\r');
        //buf.writeByte('\n');

        System.err.println( "sending event: " + event.toJSON().build());
        System.err.println( "content event: " + buf.toString(Charset.forName("UTF-8")));
        //HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf, false);
        HttpResponse response = new DefaultFullHttpResponse(new HttpVersion("EVENT/1.0", true), HttpResponseStatus.OK, buf, false);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/hap+json");
        out.add(response);
    }
}
