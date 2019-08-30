package io.overpoet.hap.server;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.overpoet.core.concurrent.NamedThreadFactory;
import io.overpoet.hap.server.auth.ServerAuthStorage;
import io.overpoet.hap.server.model.ServerAccessoryDatabase;

/**
 * Created by bob on 8/29/18.
 */
public class HAPServer {

    public HAPServer(ServerAuthStorage authStorage, ServerAccessoryDatabase db) {
        this.authStorage = authStorage;
        this.db = db;
    }

    public int start(InetSocketAddress bind) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup(0,
                                                           new NamedThreadFactory("hap.server.loop")
        );
        ServerBootstrap b = new ServerBootstrap(); // (1)
        b.group(workerGroup); // (2)
        b.channel(NioServerSocketChannel.class); // (3)
        //b.option(ChannelOption.TCP_NODELAY, true); // (4)
        b.childHandler(new ServerInitializer(this.authStorage, this.db));

        this.channel = b.bind(bind.getAddress(), bind.getPort()).sync().channel();
        return ((InetSocketAddress)this.channel.localAddress()).getPort();
    }

    public void stop() throws InterruptedException {
        this.channel.close().sync();
    }

    public Channel channel() {
        return this.channel;
    }

    private final ServerAuthStorage authStorage;

    private final ServerAccessoryDatabase db;

    private Channel channel;
}
