package io.overpoet.hap.server.codec;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.json.spi.JsonProvider;

import io.overpoet.hap.common.model.Accessory;
import io.overpoet.hap.common.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.overpoet.hap.server.model.ServerAccessory;

/**
 * Created by bob on 9/11/18.
 */
public class AccessoriesRequestHandler extends ChannelInboundHandlerAdapter {

    public AccessoriesRequestHandler(ServerAccessory accessory) {
        this.accessory = accessory;
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
        //byte[] bytes = "{\"accessories\": []}".getBytes(StandardCharsets.UTF_8);
        JsonObjectBuilder builder = JsonProvider.provider().createObjectBuilder();
        JsonArrayBuilder array = JsonProvider.provider().createArrayBuilder();
        array.add( this.accessory.toJSON() );
        builder.add("accessories",array);


        byte[] bytes = null;
        try ( ByteArrayOutputStream byteStream = new ByteArrayOutputStream() ) {
            JsonWriter writer = JsonProvider.provider().createWriter(byteStream);
            writer.writeObject( builder.build() );
            bytes = byteStream.toByteArray();
        }

        //System.err.println( "** RESPONSE plaintext: " + ByteUtil.toString(bytes));
        System.err.println( "accessories: " + new String(bytes));
        content.writeBytes(bytes);

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/hap+json");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        ctx.pipeline().writeAndFlush(response);
    }

    private final ServerAccessory accessory;
}
