package io.overpoet.lutron.leap.client.codec;

import java.util.concurrent.CountDownLatch;

import javax.net.ssl.SSLEngine;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.ssl.SslHandler;
import io.overpoet.lutron.leap.client.model.Universe;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    public ClientInitializer(CountDownLatch latch, SSLEngine sslEngine, Universe universe) {
        this.latch = latch;
        this.sslEngine = sslEngine;
        this.universe = universe;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        SslHandler sslHandler = new SslHandler(this.sslEngine);
        channel.pipeline().addLast(sslHandler);
        channel.pipeline().addLast(new JsonObjectDecoder(3 * 1024 * 1024));
        //channel.pipeline().addLast(new DebugHandler());
        channel.pipeline().addLast(new JSONDecoder());
        channel.pipeline().addLast(new JSONEncoder());
        channel.pipeline().addLast(new RequestEncoder());
        channel.pipeline().addLast(new ZoneDefinitionDecoder(this.universe));
        channel.pipeline().addLast(new AreaDefinitionDecoder(this.universe));
        channel.pipeline().addLast(new DeviceDefinitionDecoder(this.universe));
        channel.pipeline().addLast(new ZoneStatusDecoder(this.universe));
        channel.pipeline().addLast(new ResponseHandler(this.universe));
        //channel.pipeline().addLast(new DebugHandler());
    }

    private final SSLEngine sslEngine;
    private final Universe universe;
    private final CountDownLatch latch;
}
