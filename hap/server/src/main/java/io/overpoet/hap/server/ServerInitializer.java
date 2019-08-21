package io.overpoet.hap.server;

import io.overpoet.hap.common.codec.EncryptableMessageHandler;
import io.overpoet.hap.server.auth.ServerAuthStorage;
import io.overpoet.hap.server.codec.AccessoriesRequestHandler;
import io.overpoet.hap.server.codec.ServerPairSetupHandler;
import io.overpoet.hap.server.codec.ServerPairSetupManager;
import io.overpoet.hap.server.codec.ServerPairVerifyHandler;
import io.overpoet.hap.server.codec.ServerPairVerifyManager;
import io.overpoet.hap.common.codec.DebugHandler;
import io.overpoet.hap.common.codec.SessionCryptoHandler;
import io.overpoet.hap.common.codec.tlv.TLVDecoder;
import io.overpoet.hap.common.codec.tlv.TLVResponseEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Created by bob on 8/29/18.
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    public ServerInitializer(ServerAuthStorage authStorage) {
        this.authStorage = authStorage;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ch.pipeline().addLast(new DebugHandler("server-head"));
        ch.pipeline().addLast(new SessionCryptoHandler());
        ch.pipeline().addLast(new HttpRequestDecoder());
        ch.pipeline().addLast(new HttpResponseEncoder());
        ch.pipeline().addLast(new HttpObjectAggregator(4096));
        ch.pipeline().addLast(new EncryptableMessageHandler());
        //ch.pipeline().addLast(new DebugHandler("server-crypto-down"));
        //ch.pipeline().addLast(new DebugHandler("server-crypto-up"));

        ch.pipeline().addLast(new TLVResponseEncoder());
        ch.pipeline().addLast(new TLVDecoder());
        ch.pipeline().addLast(new DebugHandler("server-tlv"));
        ch.pipeline().addLast("Pair-Setup", new ServerPairSetupHandler(new ServerPairSetupManager(this.authStorage)));
        ch.pipeline().addLast("Pair-Verify", new ServerPairVerifyHandler(new ServerPairVerifyManager(this.authStorage)));

        ch.pipeline().addLast(new AccessoriesRequestHandler());


        ch.pipeline().addLast(new DebugHandler("server-tail"));

    }

    private final ServerAuthStorage authStorage;
}
