package io.overpoet.lutron.leap.client.codec;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.json.JsonArray;
import javax.json.JsonObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.overpoet.lutron.leap.client.model.Device;
import io.overpoet.lutron.leap.client.model.Universe;
import io.overpoet.lutron.leap.client.model.Zone;

public class DeviceDefinitionDecoder extends MessageToMessageDecoder<JsonObject> {

    public DeviceDefinitionDecoder(Universe universe) {
        this.universe = universe;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, JsonObject msg, List<Object> out) throws Exception {
        String communiqueType = msg.getString("CommuniqueType");
        if (communiqueType.equals("ReadResponse")) {
            JsonObject header = msg.getJsonObject("Header");
            String messageBodyType = header.getString("MessageBodyType");
            JsonObject body = msg.getJsonObject("Body");
            if (messageBodyType.equals("MultipleDeviceDefinition")) {
                out.addAll(parseDevices(body));
            } else if (messageBodyType.equalsIgnoreCase("SingleDeviceDefinition")) {
                out.add(parseDevice(body));
            } else {
                out.add(msg);
            }
        } else {
            out.add(msg);
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
        Device device = this.universe.device(href);
        device.name(json.getString("Name"));
        device.deviceType(json.getString("DeviceType"));
        device.serialNumber("" + json.getInt("SerialNumber"));
        device.modelNumber(json.getString("ModelNumber"));
        JsonArray zonesJson = json.getJsonArray("LocalZones");
        if (zonesJson != null) {
            Set<Zone> zones = new HashSet<>();
            for (int i = 0; i < zonesJson.size(); ++i) {
                zones.add(this.universe.zone(zonesJson.getJsonObject(i).getString("href")));
            }
            device.zones(zones);
        }
        JsonObject areaJson = json.getJsonObject("AssociatedArea");
        if (areaJson != null) {
            device.area(this.universe.area(areaJson.getString("href")));
        }
        return device;
    }

    private final Universe universe;
}
