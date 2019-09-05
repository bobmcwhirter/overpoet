package io.overpoet.lutron.caseta.bridge.codec;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.overpoet.lutron.caseta.bridge.protocol.AbstractRequestMessage;

public class RequestEncoder extends MessageToMessageEncoder<AbstractRequestMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractRequestMessage msg, List<Object> out) throws Exception {
        out.add(msg.toJSON());
    }
}
