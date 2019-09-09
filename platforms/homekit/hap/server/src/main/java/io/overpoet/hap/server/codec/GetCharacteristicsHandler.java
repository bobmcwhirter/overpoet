package io.overpoet.hap.server.codec;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.overpoet.hap.common.codec.json.JSONResponse;
import io.overpoet.hap.server.model.ServerAccessoryDatabase;
import io.overpoet.hap.server.model.impl.ServerCharacteristicImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetCharacteristicsHandler extends ChannelInboundHandlerAdapter {
    private static Logger LOG = LoggerFactory.getLogger("overpoet.hap.server");

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

        LOG.debug(ctx.channel().id() + " GET " + uri);
        QueryStringDecoder decoder = new QueryStringDecoder(httpMsg.uri());
        List<String> idParam = decoder.parameters().get("id");
        List<ServerCharacteristicImpl> characteristics = idParam.stream().flatMap(e -> Arrays.stream(e.split(","))).map(e -> {
            String[] parts = e.split("\\.");
            int aid = Integer.parseInt(parts[0]);
            int iid = Integer.parseInt(parts[1]);
            ServerCharacteristicImpl chr = db.findCharacteristic(aid, iid);
            LOG.debug(ctx.channel().id() + " Sending characteristic: " + aid + "." + iid + " (" + chr.getService().getName() + "::" + chr.getType() + ")");
            return chr;
        }).collect(Collectors.toList());

        JsonObjectBuilder builder = JsonProvider.provider().createObjectBuilder();
        builder.add("characteristics", characteristicsToJSON(characteristics));

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
