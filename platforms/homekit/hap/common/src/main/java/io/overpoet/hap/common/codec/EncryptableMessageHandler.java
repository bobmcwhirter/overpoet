package io.overpoet.hap.common.codec;

import io.overpoet.hap.common.codec.pair.SessionKeys;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.LastHttpContent;

public class EncryptableMessageHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        super.write(ctx, msg, promise);

        if (msg instanceof SessionKeys) {
            this.enabled = true;
        }

        if (msg instanceof LastHttpContent) {
            super.write(ctx, new EncryptableMessageComplete(), ctx.voidPromise());
        }
    }

    private boolean enabled = false;
}
