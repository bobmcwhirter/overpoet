package io.overpoet.hap.common.codec;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.HttpContent;
import io.overpoet.hap.common.codec.tlv.TLV;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by bob on 8/27/18.
 */
public class TLVDebugHandler extends ChannelDuplexHandler {

    public TLVDebugHandler(String tag) {
        this.tag = tag;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof TLV) {
            System.err.println("Inbound: " + msg);
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
        if (msg instanceof TLV) {
            System.err.println("Outbound: " + msg);
        }
        super.write(ctx, msg, promise);
    }

    private final String tag;
}
