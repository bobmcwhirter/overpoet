package io.overpoet.hap.server.codec;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.spi.JsonProvider;
import javax.json.stream.JsonGenerator;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInboundInvoker;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.overpoet.hap.common.codec.json.JSONRequest;
import io.overpoet.hap.common.codec.json.JSONResponse;
import io.overpoet.hap.server.model.ServerAccessoryDatabase;
import io.overpoet.hap.server.model.impl.ServerCharacteristicImpl;

public class GetCharacteristicsHandler extends ChannelInboundHandlerAdapter {
    public GetCharacteristicsHandler(ServerAccessoryDatabase db) {
        this.db = db;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof FullHttpRequest)) {
            super.channelRead(ctx, msg);
            return;
        }

        FullHttpRequest httpMsg = (FullHttpRequest) msg;
        String uri = httpMsg.uri();

        if (!uri.startsWith("/characteristics")) {
            super.channelRead(ctx, msg);
            return;
        }
        if (httpMsg.method() != HttpMethod.GET) {
            super.channelRead(ctx, msg);
            return;
        }

        System.err.println("BAR: GET /characteristics");
        QueryStringDecoder decoder = new QueryStringDecoder(httpMsg.uri());
        List<String> idParam = decoder.parameters().get("id");
        List<ServerCharacteristicImpl> characteristics = idParam.stream().flatMap(e -> Arrays.stream(e.split(","))).map(e -> {
            System.err.println("ID: " + e);
            String[] parts = e.split("\\.");
            int aid = Integer.parseInt(parts[0]);
            int iid = Integer.parseInt(parts[1]);
            System.err.println("get: " + aid + " :: " + iid);
            return db.findCharacteristic(aid, iid);
        }).collect(Collectors.toList());

        System.err.println(characteristics);

        JsonObjectBuilder builder = JsonProvider.provider().createObjectBuilder();
        builder.add("characteristics", characteristicsToJSON(characteristics));

        System.err.println("sending characteristics GET " + characteristics);
        ctx.pipeline().writeAndFlush(new JSONResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, builder.build()));
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
