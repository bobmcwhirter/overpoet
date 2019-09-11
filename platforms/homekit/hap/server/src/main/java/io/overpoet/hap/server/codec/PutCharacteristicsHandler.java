package io.overpoet.hap.server.codec;

import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.overpoet.hap.common.codec.json.JSONRequest;
import io.overpoet.hap.server.model.ServerAccessoryDatabase;
import io.overpoet.hap.server.model.impl.ServerCharacteristicImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PutCharacteristicsHandler extends ChannelInboundHandlerAdapter {

    private static Logger LOG = LoggerFactory.getLogger("overpoet.hap.server");

    public PutCharacteristicsHandler(ServerAccessoryDatabase db) {
        this.db = db;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof JSONRequest)) {
            super.channelRead(ctx, msg);
            return;
        }

        JSONRequest request = (JSONRequest) msg;

        FullHttpRequest httpMsg = ((JSONRequest) msg).httpRequest();

        String uri = httpMsg.uri();

        if (!uri.startsWith("/characteristics")) {
            super.channelRead(ctx, msg);
            return;
        }
        if (httpMsg.method() != HttpMethod.PUT) {
            super.channelRead(ctx, msg);
            return;
        }

        LOG.debug(ctx.channel().id() + " PUT /characteristics");

        JsonObject obj = request.objectContent();
        JsonArray characteristics = obj.getJsonArray("characteristics");
        for (int i = 0; i < characteristics.size(); ++i) {
            JsonObject each = characteristics.getJsonObject(i);
            System.err.println( each );
            int aid = each.getInt("aid");
            int iid = each.getInt("iid");
            if (each.containsKey("ev")) {
                boolean ev = each.getBoolean("ev");
                if (ev) {
                    LOG.debug(ctx.channel().id() + " enable events on {}:{}", aid, iid );
                    ServerCharacteristicImpl chr = this.db.findCharacteristic(aid, iid);
                    chr.addChangeListener((c) -> {
                        LOG.debug(ctx.channel().id() + " sending event on {}:{} -> {}", aid, iid, c.getValue());
                        ctx.pipeline().writeAndFlush(new Event((ServerCharacteristicImpl) c));
                    });
                    ctx.pipeline().writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NO_CONTENT));
                }
            }
            if (each.containsKey("value")) {
                ServerCharacteristicImpl chr = this.db.findCharacteristic(aid, iid);
                LOG.debug( "updating value {}:{} -> {}", aid, iid, each.get("value"));
                chr.requestValueUpdate(each.get("value"));
                ctx.pipeline().writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NO_CONTENT));
            }
        }
    }

    private final ServerAccessoryDatabase db;
}
