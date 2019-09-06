package io.overpoet.lutron.leap.client.codec;

import java.util.List;

import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.spi.JsonProvider;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class JSONDecoder extends MessageToMessageDecoder<ByteBuf>  {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        try (JsonReader reader = JsonProvider.provider().createReader(new ByteBufInputStream(msg)) ) {
            JsonObject obj = reader.readObject();
            out.add( obj );
        }
    }
}
