package io.overpoet.hap.server.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.overpoet.hap.common.codec.json.JSONResponse;
import io.overpoet.hap.server.model.ServerAccessoryDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bob on 9/11/18.
 */
public class AccessoriesRequestHandler extends ChannelInboundHandlerAdapter {
    private static Logger LOG = LoggerFactory.getLogger("overpoet.hap.server");

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

        LOG.debug("GET /accessories");

        ctx.pipeline().writeAndFlush(new JSONResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, this.db.toJSON().build()));
        ((FullHttpRequest) msg).release();
    }

    private final ServerAccessoryDatabase db;
}
