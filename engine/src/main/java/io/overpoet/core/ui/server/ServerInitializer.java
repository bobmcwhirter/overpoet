package io.overpoet.core.ui.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.overpoet.core.ui.UIManager;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    public ServerInitializer(UIManager uiManager) {
        this.uiManager = uiManager;
    }
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new HttpRequestDecoder());
        ch.pipeline().addLast(new HttpResponseEncoder());
        ch.pipeline().addLast(new HttpObjectAggregator(1024 * 512));
        ch.pipeline().addLast(new DispatchHandler(this.uiManager) );
        ch.pipeline().addLast();
    }

    private final UIManager uiManager;
}
