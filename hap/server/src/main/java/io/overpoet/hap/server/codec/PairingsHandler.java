package io.overpoet.hap.server.codec;

import java.util.Optional;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.overpoet.hap.common.codec.tlv.TLV;
import io.overpoet.hap.common.codec.tlv.Type;

public class PairingsHandler extends ChannelInboundHandlerAdapter {
    public PairingsHandler(PairingsManager manager) {
        this.manager = manager;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof TLV)) {
            super.channelRead(ctx, msg);
            return;
        }
        TLV tlv = (TLV) msg;
        String path = tlv.getAttachment(TLV.HTTP_REQUEST_PATH);
        if (path == null) {
            super.channelRead(ctx, msg);
            return;
        }

        if (!path.equals("/pairings")) {
            super.channelRead(ctx, msg);
            return;
        }

        TLV result = this.manager.handle(tlv);
        if (result != null) {
            Optional<Integer> err = Type.ERROR.get(result);
            boolean shouldClose = false;
            if ( err.isPresent() ) {
                this.manager.reset();
                shouldClose = true;
            }
            ctx.pipeline().writeAndFlush(result);
            if (shouldClose) {
                ctx.pipeline().close();
            }
        }
    }


    private final PairingsManager manager;
}
