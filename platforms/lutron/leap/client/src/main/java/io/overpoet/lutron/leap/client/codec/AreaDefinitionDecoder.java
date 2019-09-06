package io.overpoet.lutron.leap.client.codec;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.overpoet.lutron.leap.client.model.Area;
import io.overpoet.lutron.leap.client.model.Universe;

public class AreaDefinitionDecoder extends MessageToMessageDecoder<JsonObject> {

    public AreaDefinitionDecoder(Universe universe) {
        this.universe = universe;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, JsonObject msg, List<Object> out) throws Exception {
        String communiqueType = msg.getString("CommuniqueType");
        if (communiqueType.equals("ReadResponse")) {
            JsonObject header = msg.getJsonObject("Header");
            String messageBodyType = header.getString("MessageBodyType");
            JsonObject body = msg.getJsonObject("Body");
            if (messageBodyType.equalsIgnoreCase("MultipleAreaDefinition")) {
                out.addAll(parseAreas(body));
            } else if (messageBodyType.equalsIgnoreCase("OneAreaDefinition")) {
                out.add(parseArea(body));
            } else {
                out.add(msg);
            }
        } else {
            out.add(msg);
        }
    }

    protected List<Area> parseAreas(JsonObject body) {
        List<Area> results = new ArrayList<>();

        JsonArray areas = body.getJsonArray("Areas");
        for (int i = 0; i < areas.size(); ++i) {
            results.add(parseArea(areas.getJsonObject(i)));
        }
        return results;
    }

    protected Area parseArea(JsonObject json) {
        String href = json.getString("href");
        Area area = this.universe.area(href);
        area.name(json.getString("Name"));
        return area;
    }

    private final Universe universe;
}
