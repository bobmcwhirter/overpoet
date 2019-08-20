package io.overpoet.hap.client;

import io.overpoet.hap.client.codec.CharacteristicEventsRequestEncoder;
import io.overpoet.hap.client.codec.ClientPairSetupHandler;
import io.overpoet.hap.client.codec.ClientPairSetupManager;
import io.overpoet.hap.client.codec.ClientPairVerifyHandler;
import io.overpoet.hap.client.codec.ClientPairVerifyManager;
import io.overpoet.hap.client.codec.EventDecodingHandler;
import io.overpoet.hap.client.codec.UpdateCharacteristicRequestEncoder;
import io.overpoet.hap.common.codec.DebugHandler;
import io.overpoet.hap.common.codec.EncryptableMessageHandler;
import io.overpoet.hap.common.codec.SessionCryptoHandler;
import io.overpoet.hap.common.codec.tlv.TLVDecoder;
import io.overpoet.hap.common.codec.tlv.TLVRequestEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

/**
 * Created by bob on 8/27/18.
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    public ClientInitializer(ClientPairSetupManager pairSetupManager, ClientPairVerifyManager pairVerifyManager) {
        this.pairSetupManager = pairSetupManager;
        this.pairVerifyManager = pairVerifyManager;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //ch.pipeline().addLast(new DebugHandler("client-head"));
        ch.pipeline().addLast(new SessionCryptoHandler());
        ch.pipeline().addLast(new DebugHandler("client-http-after-encoded"));
        ch.pipeline().addLast(new HttpRequestEncoder());
        //ch.pipeline().addLast(new DebugHandler("client-http-before-encoded"));
        ch.pipeline().addLast(new HttpResponseDecoder());
        ch.pipeline().addLast(new EncryptableMessageHandler());
        ch.pipeline().addLast(new HttpObjectAggregator(4096));
        //ch.pipeline().addLast(new DebugHandler("client-http"));
        ch.pipeline().addLast(new EventDecodingHandler());

        ch.pipeline().addLast(new TLVRequestEncoder());
        ch.pipeline().addLast(new TLVDecoder());

        ch.pipeline().addLast("Pair-Setup", new ClientPairSetupHandler(this.pairSetupManager));
        ch.pipeline().addLast("Pair-Verify", new ClientPairVerifyHandler(this.pairVerifyManager));

        ch.pipeline().addLast(new UpdateCharacteristicRequestEncoder());
        ch.pipeline().addLast(new CharacteristicEventsRequestEncoder());

        ch.pipeline().addLast(new SyncRequestCompleter());
    }

    private final ClientPairVerifyManager pairVerifyManager;

    private final ClientPairSetupManager pairSetupManager;
}
