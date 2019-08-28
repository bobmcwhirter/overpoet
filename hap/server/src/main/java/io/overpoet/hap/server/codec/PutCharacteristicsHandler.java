package io.overpoet.hap.server.codec;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.overpoet.hap.common.codec.json.JSONRequest;
import io.overpoet.hap.common.codec.json.JSONResponse;
import io.overpoet.hap.server.model.ServerAccessoryDatabase;
import io.overpoet.hap.server.model.impl.ServerCharacteristicImpl;

public class PutCharacteristicsHandler extends ChannelInboundHandlerAdapter {
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

        System.err.println("CHARACTERISTICS QUERY");
        System.err.println("type: " + httpMsg.method());

        System.err.println("BAR: FOO /characteristics");
        JsonObject obj = request.objectContent();
        JsonArray characteristics = obj.getJsonArray("characteristics");
        for (int i = 0; i < characteristics.size(); ++i) {
            JsonObject each = characteristics.getJsonObject(i);
            int aid = each.getInt("aid");
            int iid = each.getInt("iid");
            if (each.containsKey("ev")) {
                boolean ev = each.getBoolean("ev");
                if (ev) {
                    ctx.pipeline().writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NO_CONTENT));
                    System.err.println("enable events on : " + aid + "/" + iid);
                    ServerCharacteristicImpl chr = this.db.findCharacteristic(aid, iid);
                    System.err.println("enabling on: " + chr);
                    chr.addListener((c) -> {
                        System.err.println("SEND EVENT ON " + c);
                        ctx.pipeline().writeAndFlush(new Event((ServerCharacteristicImpl) c));
                    });
                }
            }
        }
    }

    private JsonArrayBuilder characteristicsToJSON(List<ServerCharacteristicImpl> characteristics) {
        JsonArrayBuilder builder = JsonProvider.provider().createArrayBuilder();

        for (ServerCharacteristicImpl characteristic : characteristics) {
            builder.add(characteristic.toJSON(true));
        }

        return builder;
    }

    private final ServerAccessoryDatabase db;
}
