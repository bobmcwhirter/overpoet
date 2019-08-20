package com.overpoet.hap.client.impl;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.overpoet.hap.client.codec.ClientPairSetupManager;
import com.overpoet.hap.client.codec.ClientPairVerifyManager;
import com.overpoet.hap.client.codec.SRPStartRequest;
import com.overpoet.hap.client.codec.VerifyStartRequest;
import com.overpoet.hap.client.ClientInitializer;
import com.overpoet.hap.client.Connection;
import com.overpoet.hap.client.SimpleClient;
import com.overpoet.hap.client.PairedConnection;
import com.overpoet.hap.client.PinSupplier;
import com.overpoet.hap.client.SimplePinSupplier;
import com.overpoet.hap.client.auth.ClientAuthStorage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by bob on 8/27/18.
 */
public class ConnectionImpl implements Connection {

    public ConnectionImpl(SimpleClient client, InetSocketAddress socketAddress, ClientAuthStorage authStorage) {
        this.client = client;
        this.socketAddress = socketAddress;
        this.pairSetupManager = new ClientPairSetupManager(authStorage);
        this.pairVerifyManager = new ClientPairVerifyManager(authStorage);
    }

    public CompletableFuture<Connection> connect() throws InterruptedException {
        Bootstrap b = new Bootstrap(); // (1)
        b.group(client.getWorkerGroup()); // (2)
        b.channel(NioSocketChannel.class); // (3)
        //b.option(ChannelOption.TCP_NODELAY, true); // (4)
        b.handler(new ClientInitializer(this.pairSetupManager, this.pairVerifyManager));
        b.attr(SimpleClient.CLIENT, this.client);

        CompletableFuture<Connection> f = new CompletableFuture<>();
        // Start the client.
        b.connect(this.socketAddress.getAddress(),
                  this.socketAddress.getPort())
                .addListener((res) -> {
                    this.channel = ((ChannelFuture) res).channel();
                    f.complete(this);
                });

        return f;
    }

    public PairedConnection pair(String pin) throws ExecutionException, InterruptedException {
        return pair(new SimplePinSupplier(pin));
    }

    public PairedConnection pair(PinSupplier pinSupplier) throws ExecutionException, InterruptedException {
        SRPStartRequest request = new SRPStartRequest(pinSupplier);
        this.channel.writeAndFlush(request);
        return request.getFuture().get();
    }

    public PairedConnection pairVerify() throws ExecutionException, InterruptedException {
        CompletableFuture<PairedConnection> future = new CompletableFuture<>();
        this.channel.writeAndFlush(new VerifyStartRequest(future));
        return future.get();
    }

    private final InetSocketAddress socketAddress;

    private final ClientPairSetupManager pairSetupManager;

    private final ClientPairVerifyManager pairVerifyManager;

    private final SimpleClient client;

    private Channel channel;
}
