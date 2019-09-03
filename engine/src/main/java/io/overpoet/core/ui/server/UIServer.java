package io.overpoet.core.ui.server;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.overpoet.core.concurrent.NamedThreadFactory;
import io.overpoet.core.ui.UIManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UIServer {

    private final Logger LOG = LoggerFactory.getLogger("overpoet.ui");

    public UIServer(UIManager uiManager) {
        this.uiManager = uiManager;
    }

    public int start(InetSocketAddress bind) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup(0,
                                                           new NamedThreadFactory("ui.loop")
        );
        ServerBootstrap b = new ServerBootstrap(); // (1)
        b.group(workerGroup); // (2)
        b.channel(NioServerSocketChannel.class); // (3)
        //b.option(ChannelOption.TCP_NODELAY, true); // (4)
        b.childHandler(new ServerInitializer(this.uiManager));

        this.channel = b.bind(bind.getAddress(), bind.getPort()).sync().channel();
        return ((InetSocketAddress)this.channel.localAddress()).getPort();
    }

    public void stop() throws InterruptedException {
        LOG.info("stopping UI");
        this.channel.close().sync();
        LOG.info("stopped UI");
    }

    public Channel channel() {
        return this.channel;
    }

    private final UIManager uiManager;
    private Channel channel;
}
