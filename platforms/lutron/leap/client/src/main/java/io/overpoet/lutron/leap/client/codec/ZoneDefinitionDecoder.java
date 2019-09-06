package io.overpoet.lutron.leap.client.codec;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.overpoet.lutron.leap.client.model.ControlType;
import io.overpoet.lutron.leap.client.model.Zone;
import io.overpoet.lutron.leap.client.model.Universe;

public class ZoneDefinitionDecoder extends MessageToMessageDecoder<JsonObject> {

    public ZoneDefinitionDecoder(Universe universe) {
        this.universe = universe;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, JsonObject msg, List<Object> out) throws Exception {
        String communiqueType = msg.getString("CommuniqueType");
        if (communiqueType.equals("ReadResponse")) {
            JsonObject header = msg.getJsonObject("Header");
            String messageBodyType = header.getString("MessageBodyType");
            JsonObject body = msg.getJsonObject("Body");
            if (messageBodyType.equalsIgnoreCase("MultipleZoneDefinition")) {
                System.err.println("");
                System.err.println("");
                System.err.println(msg);
                System.err.println("");
                System.err.println("");
                out.addAll(parseZones(body));
            } else if (messageBodyType.equalsIgnoreCase("SingleZoneDefinition")) {
                out.add(parseZone(body));
            } else {
                out.add(msg);
            }
        } else {
            out.add(msg);
        }
    }

    protected List<Zone> parseZones(JsonObject body) {
        List<Zone> results = new ArrayList<>();
        JsonArray zones = body.getJsonArray("Zones");
        for (int i = 0; i < zones.size(); ++i) {
            results.add(parseZone(zones.getJsonObject(i)));
        }
        return results;
    }

    protected Zone parseZone(JsonObject json) {
        String href = json.getString("href");
        Zone zone = this.universe.zone(href);
        zone.name(json.getString("Name"));
        zone.controlType(ControlType.of(json.getString("ControlType")));
        JsonObject deviceJson = json.getJsonObject("Device");
        String deviceHref = deviceJson.getString("href");
        zone.device(this.universe.device(deviceHref));
        return zone;
    }

    private final Universe universe;
}
