package io.overpoet.hap.client.model.parse;

import java.io.IOException;
import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

import io.overpoet.hap.client.impl.AccessoryDBImpl;
import io.overpoet.hap.client.impl.ClientAccessoryImpl;
import io.overpoet.hap.client.impl.ClientCharacteristicImpl;
import io.overpoet.hap.client.model.ClientService;
import io.overpoet.hap.common.model.Characteristic;
import io.overpoet.hap.common.model.CharacteristicType;
import io.overpoet.hap.common.model.Format;
import io.overpoet.hap.common.model.Permission;
import io.overpoet.hap.common.model.Service;
import io.overpoet.hap.common.model.ServiceType;
import io.overpoet.hap.common.model.impl.AbstractCharacteristicImpl;
import io.overpoet.hap.client.impl.EventableCharacteristicImpl;
import io.overpoet.hap.client.impl.ClientServiceImpl;
import io.overpoet.hap.common.model.Services;
import io.overpoet.hap.client.impl.PairedConnectionImpl;
import io.overpoet.hap.client.model.ClientAccessory;
import io.overpoet.hap.common.model.Characteristics;
import io.overpoet.hap.common.model.Permissions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.overpoet.hap.common.model.impl.PermissionsImpl;

public class AccessoriesParser {

    public AccessoriesParser(PairedConnectionImpl pairedConnection) {
        this.pairedConnection = pairedConnection;
    }

    public AccessoryDBImpl parse(ByteBuf buf) throws IOException {

        AccessoryDBImpl accessories = new AccessoryDBImpl(this.pairedConnection);
        try (InputStream in = new ByteBufInputStream(buf)) {
            JsonReader reader = Json.createReader(in);
            JsonObject object = reader.readObject();

            JsonArray accessoriesJson = object.getJsonArray("accessories");

            accessoriesJson.forEach(e -> {
                accessories.addAccessory(parseAccessory(accessories, (JsonObject) e));
            });
        }

        return accessories;
    }

    protected ClientAccessory parseAccessory(AccessoryDBImpl accessories, JsonObject json) {
        int aid = json.getInt("aid");
        ClientAccessoryImpl accessory = new ClientAccessoryImpl(accessories, aid);
        json.getJsonArray("services").forEach(e -> {
            accessory.addService(parseService(accessory, (JsonObject) e));
        });
        return accessory;
    }

    protected ClientService parseService(ClientAccessoryImpl accessory, JsonObject json) {
        int iid = json.getInt("iid");
        String type = json.getString("type");
        ServiceType serviceType = Services.lookup(type);
        ClientServiceImpl service = new ClientServiceImpl(accessory, iid, serviceType);
        json.getJsonArray("characteristics").forEach(e -> {
            service.addCharacteristic(parseCharacteristic(service, (JsonObject) e));
        });

        return service;
    }

    protected Characteristic parseCharacteristic(ClientServiceImpl service, JsonObject json) {
        int iid = json.getInt("iid");
        String type = json.getString("type");

        CharacteristicType characteristicType = Characteristics.lookup(type);
        Permissions permissions = parsePermissions(json.getJsonArray("perms"));

        AbstractCharacteristicImpl characteristic = null;

        if (permissions.contains(Permission.NOTIFY)) {
            characteristic = new EventableCharacteristicImpl(service, iid, characteristicType);
        } else {
            characteristic = new ClientCharacteristicImpl(service, iid, characteristicType);
        }

        characteristic.setPermissions(parsePermissions(json.getJsonArray("perms")));

        JsonValue value = json.get("value");
        if (value != null) {
            JsonValue.ValueType valueType = value.getValueType();

            switch (valueType) {
                case ARRAY:
                    break;
                case OBJECT:
                    break;
                case STRING:
                    characteristic.setStoredValue(json.getString("value"));
                    break;
                case NUMBER:
                    if (characteristicType.getFormat() == Format.FLOAT) {
                        characteristic.setStoredValue(json.getJsonNumber("value").doubleValue());
                    } else {
                        characteristic.setStoredValue(json.getJsonNumber("value").longValue());
                    }
                    break;
                case TRUE:
                    characteristic.setStoredValue(Boolean.TRUE);
                    break;
                case FALSE:
                    characteristic.setStoredValue(Boolean.FALSE);
                    break;
                case NULL:
                    break;
            }
        }

        return characteristic;
    }

    private Permissions parsePermissions(JsonArray json) {
        PermissionsImpl permissions = new PermissionsImpl();
        json.forEach(e -> {
            permissions.addPermission(parsePermission((JsonString) e));
        });
        return permissions;
    }

    private Permission parsePermission(JsonString json) {
        String str = json.getString();
        if (str.equals("pr")) {
            return Permission.PAIRED_READ;
        }
        if (str.equals("pw")) {
            return Permission.PAIRED_WRITE;
        }
        if (str.equals("ev")) {
            return Permission.NOTIFY;
        }
        return null;
    }

    private final PairedConnectionImpl pairedConnection;
}
