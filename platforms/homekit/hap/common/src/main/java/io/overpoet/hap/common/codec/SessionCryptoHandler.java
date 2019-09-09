package io.overpoet.hap.common.codec;

import java.util.List;

import io.overpoet.hap.common.codec.pair.SessionKeys;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by bob on 8/30/18.
 */
public class SessionCryptoHandler extends ChannelDuplexHandler {
    private static Logger LOG = LoggerFactory.getLogger(SessionCryptoHandler.class);

    public SessionCryptoHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (this.keys != null && msg instanceof ByteBuf) {
            ByteBuf original = (ByteBuf) msg;
            List<ByteBuf> decrypted = this.keys.decrypt(original);
            for (ByteBuf e : decrypted) {
                super.channelRead(ctx, e);
            }
            ;
        } else {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof SessionKeys) {
            this.keys = (SessionKeys) msg;
            return;
        } else if (this.keys != null && msg instanceof ByteBuf) {
            ByteBuf original = (ByteBuf) msg;
            this.keys.encrypt(original);
            original.release();
            return;
        } else if (this.keys != null && msg instanceof EncryptableMessageComplete) {
            List<ByteBuf> result = this.keys.doEncrypt();
            int numChunks = result.size();
            for (int i = 0; i < numChunks; ++i) {
                if (i + 1 == numChunks) {
                    super.write(ctx, result.get(i), promise);
                } else {
                    super.write(ctx, result.get(i), ctx.voidPromise());
                }
            }
            return;
        }
        super.write(ctx, msg, promise);
    }

    private SessionKeys keys;
}
