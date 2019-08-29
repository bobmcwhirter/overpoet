package io.overpoet.core.ui.impl.server;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.overpoet.core.ui.impl.UIManager;
import org.jetbrains.annotations.NotNull;

public class UIServer {

    public UIServer(UIManager uiManager) {
        this.uiManager = uiManager;
    }

    public int start(InetSocketAddress bind) throws InterruptedException {
        ThreadGroup group = new ThreadGroup("ui");
        ThreadFactory threadFactory = new ThreadFactory() {
            private AtomicInteger counter = new AtomicInteger();
            @Override
            public Thread newThread(@NotNull Runnable r) {
                return new Thread(group, r, "ui.event-loop-" + counter.incrementAndGet());
            }
        };
        EventLoopGroup workerGroup = new NioEventLoopGroup(0, threadFactory);
        ServerBootstrap b = new ServerBootstrap(); // (1)
        b.group(workerGroup); // (2)
        b.channel(NioServerSocketChannel.class); // (3)
        //b.option(ChannelOption.TCP_NODELAY, true); // (4)
        b.childHandler(new ServerInitializer(this.uiManager));

        this.channel = b.bind(bind.getAddress(), bind.getPort()).sync().channel();
        return ((InetSocketAddress)this.channel.localAddress()).getPort();
    }

    public Channel channel() {
        return this.channel;
    }

    private final UIManager uiManager;
    private Channel channel;
}
