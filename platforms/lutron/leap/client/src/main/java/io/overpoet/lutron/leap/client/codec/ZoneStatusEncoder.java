package io.overpoet.lutron.leap.client.codec;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.overpoet.lutron.leap.client.model.SwitchedLevel;
import io.overpoet.lutron.leap.client.model.ZoneStatus;
import io.overpoet.lutron.leap.client.protocol.GoToLevel;

public class ZoneStatusEncoder extends MessageToMessageEncoder<ZoneStatus> {

    public ZoneStatusEncoder() {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ZoneStatus msg, List<Object> out) throws Exception {
        if ( msg.switchedLevel() != null ) {
            //out.add(new GoToLevel(msg.zone(), msg.switchedLevel() == SwitchedLevel.ON ? 100 : 0 ));
            out.add(new GoToLevel(msg.zone(), msg.switchedLevel()));
        }
        if ( msg.level() != null ) {
            out.add( new GoToLevel(msg.zone(), msg.level()));
        }
    }

}
