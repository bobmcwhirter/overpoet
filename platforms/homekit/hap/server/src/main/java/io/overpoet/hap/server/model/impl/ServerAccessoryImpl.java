package io.overpoet.hap.server.model.impl;

import java.util.function.Consumer;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

import io.overpoet.hap.common.model.Accessory;
import io.overpoet.hap.common.model.Service;
import io.overpoet.hap.common.model.ServiceType;
import io.overpoet.hap.common.model.impl.AbstractAccessoryImpl;
import io.overpoet.hap.server.model.ServerAccessory;

public class ServerAccessoryImpl extends AbstractAccessoryImpl<ServerServiceImpl> implements ServerAccessory {

    public ServerAccessoryImpl(int aid, Consumer<ServerAccessoryImpl> config) {
        super(aid);
        config.accept(this);
    }

    public void addService(int iid, ServiceType type, Consumer<ServerServiceImpl> config) {
        addService(new ServerServiceImpl(this, iid, type, config));
    }

    public JsonObjectBuilder toJSON() {
        JsonObjectBuilder builder = JsonProvider.provider().createObjectBuilder();
        builder.add("aid", getAID() );
        builder.add( "services", servicesToJSON() );
        return builder;
    }

    public JsonArrayBuilder servicesToJSON() {
        JsonArrayBuilder builder = JsonProvider.provider().createArrayBuilder();

        for (ServerServiceImpl service : getServices()) {
            builder.add( service.toJSON() );
        }

        return builder;
    }
}
