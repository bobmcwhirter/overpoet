package io.overpoet.hap.server.codec;

import java.util.List;

import javax.json.JsonWriter;
import javax.json.spi.JsonProvider;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bob on 8/28/18.
 */
public class EventEncoder extends MessageToMessageEncoder<Event> {
    private static Logger LOG = LoggerFactory.getLogger(EventEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Event event, List<Object> out) throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        try (ByteBufOutputStream os = new ByteBufOutputStream(buf)) {
            JsonWriter writer = JsonProvider.provider().createWriter(os);
            writer.writeObject(event.toJSON().build());
            writer.close();
        }

        HttpResponse response = new DefaultFullHttpResponse(new HttpVersion("EVENT/1.0", true), HttpResponseStatus.OK, buf, false);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/hap+json");
        out.add(response);
    }
}
