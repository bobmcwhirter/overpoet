package io.overpoet.lutron.caseta.bridge.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.overpoet.lutron.caseta.bridge.model.Device;
import io.overpoet.lutron.caseta.bridge.model.Zone;

public class ResponseHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ( msg instanceof Zone) {
            System.err.println( "zone: " + msg);
        }
        if ( msg instanceof Device) {
            System.err.println( "device: " + msg);
        }
        super.channelRead(ctx, msg);
    }
}
