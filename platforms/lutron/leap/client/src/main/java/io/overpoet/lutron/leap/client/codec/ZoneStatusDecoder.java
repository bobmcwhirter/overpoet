package io.overpoet.lutron.leap.client.codec;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.overpoet.lutron.leap.client.model.SwitchedLevel;
import io.overpoet.lutron.leap.client.model.Universe;
import io.overpoet.lutron.leap.client.model.ZoneStatus;

public class ZoneStatusDecoder extends MessageToMessageDecoder<JsonObject> {

    public ZoneStatusDecoder(Universe universe) {
        this.universe = universe;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, JsonObject msg, List<Object> out) throws Exception {
        String communiqueType = msg.getString("CommuniqueType");
        if (communiqueType.equals("ReadResponse")) {
            JsonObject header = msg.getJsonObject("Header");
            String messageBodyType = header.getString("MessageBodyType");
            JsonObject body = msg.getJsonObject("Body");
            if (messageBodyType.equalsIgnoreCase("MultipleZoneStatus")) {
                out.addAll(parseZoneStatuses(body));
            } else if (messageBodyType.equalsIgnoreCase("SingleZoneStatus")) {
                out.add(parseZoneStatus(body));
            } else {
                out.add(msg);
            }
        } else {
            out.add(msg);
        }
    }

    protected List<ZoneStatus> parseZoneStatuses(JsonObject body) {
        System.err.println( "STATUS: " + body);
        List<ZoneStatus> results = new ArrayList<>();
        JsonArray statuses = body.getJsonArray("ZoneStatuses");
        for (int i = 0; i < statuses.size(); ++i) {
            results.add(parseZoneStatus(statuses.getJsonObject(i)));
        }
        return results;
    }

    protected ZoneStatus parseZoneStatus(JsonObject json) {
        JsonObject zoneJson = json.getJsonObject("Zone");
        String zoneHref = zoneJson.getString("href");
        ZoneStatus status = new ZoneStatus(this.universe.zone(zoneHref));
        if (json.containsKey("SwitchedLevel")) {
            status.switchedLevel(SwitchedLevel.of(json.getString("SwitchedLevel")));
        }
        if (json.containsKey("Level")) {
            status.level(json.getInt("Level"));
        }

        return status;
    }

    private final Universe universe;
}
