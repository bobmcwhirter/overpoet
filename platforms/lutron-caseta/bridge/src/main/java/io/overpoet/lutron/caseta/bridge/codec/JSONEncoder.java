package io.overpoet.lutron.caseta.bridge.codec;

import java.util.List;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.json.spi.JsonProvider;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class JSONEncoder extends MessageToMessageEncoder<JsonObjectBuilder>  {

    @Override
    protected void encode(ChannelHandlerContext ctx, JsonObjectBuilder msg, List<Object> out) throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        try ( JsonWriter writer = JsonProvider.provider().createWriter(new ByteBufOutputStream(buf)) ) {
            writer.writeObject(msg.build());
        }
        buf.writeByte('\r');
        buf.writeByte('\n');
        out.add(buf);
    }

}
