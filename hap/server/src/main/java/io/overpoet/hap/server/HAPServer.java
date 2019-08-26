package io.overpoet.hap.server;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.overpoet.hap.server.auth.ServerAuthStorage;
import io.overpoet.hap.server.model.ServerAccessory;

/**
 * Created by bob on 8/29/18.
 */
public class HAPServer {

    public HAPServer(ServerAuthStorage authStorage, ServerAccessory bridgeAccessory) {
        this.authStorage = authStorage;
        this.bridgeAccessory = bridgeAccessory;
    }

    public int start(InetSocketAddress bind) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap(); // (1)
        b.group(workerGroup); // (2)
        b.channel(NioServerSocketChannel.class); // (3)
        //b.option(ChannelOption.TCP_NODELAY, true); // (4)
        b.childHandler(new ServerInitializer(this.authStorage, this.bridgeAccessory));

        this.channel = b.bind(bind.getAddress(), bind.getPort()).sync().channel();
        return ((InetSocketAddress)this.channel.localAddress()).getPort();
    }

    public Channel channel() {
        return this.channel;
    }

    private final ServerAuthStorage authStorage;

    private final ServerAccessory bridgeAccessory;

    private Channel channel;
}
