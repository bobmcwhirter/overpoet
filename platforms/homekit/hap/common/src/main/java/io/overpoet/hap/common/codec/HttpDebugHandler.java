package io.overpoet.hap.common.codec;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.overpoet.hap.common.codec.tlv.TLV;

/**
 * Created by bob on 8/27/18.
 */
public class HttpDebugHandler extends ChannelDuplexHandler {

    public HttpDebugHandler() {
        this.tag = "http";
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            System.err.println("Inbound: " + msg );
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println(this.tag + " channel inactive");
        super.channelInactive(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof FullHttpResponse) {
            System.err.println("Outbound: " + msg);
        }
        super.write(ctx, msg, promise);
    }

    private final String tag;
}
