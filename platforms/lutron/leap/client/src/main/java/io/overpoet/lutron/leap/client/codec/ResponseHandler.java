package io.overpoet.lutron.leap.client.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.overpoet.lutron.leap.client.model.Universe;
import io.overpoet.lutron.leap.client.model.Zone;
import io.overpoet.lutron.leap.client.model.ZoneStatus;

public class ResponseHandler extends ChannelInboundHandlerAdapter {

    public ResponseHandler(Universe universe) {
        this.universe = universe;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        //System.err.println("HANDLE: " + msg);
        if (msg instanceof Zone) {
            universe.onZoneAdded((Zone)msg);
        } else if ( msg instanceof ZoneStatus ) {
            //universe.onZoneStatusUpdated((ZoneStatus)msg);
            ((ZoneStatus) msg).zone().statusChanged((ZoneStatus)msg);
        }
    }

    private final Universe universe;
}
