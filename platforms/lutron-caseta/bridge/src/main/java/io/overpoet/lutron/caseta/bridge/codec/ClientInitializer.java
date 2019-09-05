package io.overpoet.lutron.caseta.bridge.codec;

import javax.net.ssl.SSLEngine;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.ssl.SslHandler;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    public ClientInitializer(SSLEngine sslEngine) {
        this.sslEngine = sslEngine;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        SslHandler sslHandler = new SslHandler(this.sslEngine);
        channel.pipeline().addLast(sslHandler);
        channel.pipeline().addLast(new DebugHandler());
        channel.pipeline().addLast(new JsonObjectDecoder());
        channel.pipeline().addLast(new JSONDecoder());
        channel.pipeline().addLast(new JSONEncoder());
        channel.pipeline().addLast(new RequestEncoder());
        channel.pipeline().addLast(new DebugHandler());
    }

    private final SSLEngine sslEngine;
}
