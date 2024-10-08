package io.overpoet.hap.client.codec;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import io.overpoet.hap.client.impl.AccessoryDBImpl;
import io.overpoet.hap.client.model.AccessoryDB;
import io.overpoet.hap.client.model.parse.AccessoriesParser;
import io.overpoet.hap.client.SimpleClient;
import io.overpoet.hap.client.impl.PairedConnectionImpl;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

/**
 * Created by bob on 9/11/18.
 */
public class AccessoriesRequest extends ChannelDuplexHandler {

    public AccessoriesRequest() {
        this.future = new CompletableFuture<>();
    }

    public CompletableFuture<AccessoryDB> getFuture() {
        return this.future;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof FullHttpResponse)) {
            super.channelRead(ctx, msg);
            return;
        }

        try {
            PairedConnectionImpl pairedConn = ctx.channel().attr(SimpleClient.PAIRED_CONNECTION).get();
            AccessoryDBImpl db = new AccessoriesParser(pairedConn).parse(((FullHttpResponse) msg).content());
            this.future.complete(db);
        } catch (IOException e) {
            this.future.completeExceptionally(e);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof AccessoriesRequest) {
            DefaultFullHttpRequest httpReq = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/accessories");
            //httpReq.headers().set(HttpHeaderNames.HOST, "192.168.1.147:80");
            super.write(ctx, httpReq, promise);
        } else {
            super.write(ctx, msg, promise);
        }
    }

    private final CompletableFuture<AccessoryDB> future;
}
