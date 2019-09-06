package io.overpoet.lutron.caseta.bridge.codec;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.json.JsonArray;
import javax.json.JsonObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.overpoet.lutron.caseta.bridge.model.Area;
import io.overpoet.lutron.caseta.bridge.model.ControlType;
import io.overpoet.lutron.caseta.bridge.model.DefaultArea;
import io.overpoet.lutron.caseta.bridge.model.DefaultDevice;
import io.overpoet.lutron.caseta.bridge.model.DefaultZone;
import io.overpoet.lutron.caseta.bridge.model.Device;
import io.overpoet.lutron.caseta.bridge.model.Universe;
import io.overpoet.lutron.caseta.bridge.model.Zone;

public class ResponseDecoder extends MessageToMessageDecoder<JsonObject> {

    public ResponseDecoder(Universe universe) {
        this.universe = universe;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, JsonObject msg, List<Object> out) throws Exception {
        System.err.println("DECODE: " + msg);
        String communiqueType = msg.getString("CommuniqueType");
        if (communiqueType.equals("ReadResponse")) {
            JsonObject header = msg.getJsonObject("Header");
            String messageBodyType = header.getString("MessageBodyType");
            JsonObject body = msg.getJsonObject("Body");
            if (messageBodyType.equals("MultipleDeviceDefinition")) {
                out.addAll(parseDevices(body));
            } else if (messageBodyType.equalsIgnoreCase("MultipleAreaDefinition")) {
                out.addAll(parseAreas(body));
            } else if (messageBodyType.equalsIgnoreCase("MultipleZoneDefinition")) {
                out.addAll(parseZones(body));
            }
        }
    }

    protected List<Device> parseDevices(JsonObject body) {
        List<Device> results = new ArrayList<>();
        JsonArray devices = body.getJsonArray("Devices");
        for (int i = 0; i < devices.size(); ++i) {
            results.add(parseDevice(devices.getJsonObject(i)));
        }
        return results;
    }

    protected Device parseDevice(JsonObject json) {
        String href = json.getString("href");
        DefaultDevice device = new DefaultDevice(href);
        device.name(json.getString("Name"));
        device.deviceType(json.getString("DeviceType"));
        device.serialNumber("" + json.getInt("SerialNumber"));
        device.modelNumber(json.getString("ModelNumber"));
        JsonArray zonesJson = json.getJsonArray("LocalZones");
        if ( zonesJson != null ) {
            Set<Zone> zones = new HashSet<>();
            for (int i = 0; i < zonesJson.size(); ++i) {
                zones.add(this.universe.zone(zonesJson.getJsonObject(i).getString("href")));
            }
            device.zones(zones);
        }
        JsonObject areaJson = json.getJsonObject("AssociatedArea");
        if ( areaJson != null ) {
            device.area( this.universe.area( areaJson.getString("href")));

        }
        return this.universe.process(device);
    }

    protected List<Area> parseAreas(JsonObject body) {
        List<Area> results = new ArrayList<>();

        JsonArray areas = body.getJsonArray("Areas");
        for ( int i = 0 ; i < areas.size() ; ++i ) {
            results.add( parseArea(areas.getJsonObject(i)));
        }
        return results;
    }

    protected Area parseArea(JsonObject json) {
        String href = json.getString("href");
        DefaultArea area = new DefaultArea(href);

        area.name(json.getString("Name"));

        return universe.process(area);
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
        DefaultZone zone = new DefaultZone(href);
        zone.name(json.getString("Name"));
        zone.controlType(ControlType.of(json.getString("ControlType")));
        return universe.process(zone);
    }

    private final Universe universe;
}
