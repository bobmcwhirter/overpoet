package io.overpoet.lutron.caseta.bridge.codec;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class DebugHandler extends ChannelDuplexHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.err.println( "channel active");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println( "channel inactive");
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.err.println( "read: " + msg + " // " + msg.getClass());
        if ( msg instanceof ByteBuf ) {
            System.err.println( "[" + ((ByteBuf) msg).toString(Charset.forName("UTF-8")) + "]");
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.err.println( "write: " + msg);
        if ( msg instanceof ByteBuf ) {
            System.err.println( "[" + ((ByteBuf) msg).toString(Charset.forName("UTF-8")) + "]");
        }
        super.write(ctx, msg, promise);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        System.err.println( "flush" );
        super.flush(ctx);
    }
}
