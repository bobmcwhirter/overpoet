package io.overpoet.hap.server.codec;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.json.JsonWriter;
import javax.json.spi.JsonProvider;
import javax.json.stream.JsonGenerator;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.overpoet.hap.server.model.ServerAccessoryDatabase;

/**
 * Created by bob on 9/11/18.
 */
public class AccessoriesRequestHandler extends ChannelInboundHandlerAdapter {

    public AccessoriesRequestHandler(ServerAccessoryDatabase db) {
        this.db = db;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof FullHttpRequest)) {
            super.channelRead(ctx, msg);
            return;
        }

        String uri = ((FullHttpRequest) msg).uri();

        if (!uri.equals("/accessories")) {
            super.channelRead(ctx, msg);
            return;
        }

        System.err.println( "accessories: requested");

        ByteBuf content = ctx.alloc().buffer();

        byte[] bytes = null;
        try ( ByteArrayOutputStream byteStream = new ByteArrayOutputStream() ) {
            Map<String, Object> config = new HashMap<>();
            config.put(JsonGenerator.PRETTY_PRINTING, true);
            JsonWriter writer = JsonProvider.provider().createWriterFactory(config).createWriter(byteStream);
            writer.writeObject( this.db.toJSON().build() );
            bytes = byteStream.toByteArray();
        }

        //System.err.println( "accessories: " + new String(bytes));
        content.writeBytes(bytes);

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/hap+json");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        ctx.pipeline().writeAndFlush(response);
    }

    private final ServerAccessoryDatabase db;
}
