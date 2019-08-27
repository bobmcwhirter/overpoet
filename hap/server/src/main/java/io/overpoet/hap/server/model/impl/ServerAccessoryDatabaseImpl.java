package io.overpoet.hap.server.model.impl;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

import io.overpoet.hap.server.model.ServerAccessory;
import io.overpoet.hap.server.model.ServerAccessoryDatabase;

public class ServerAccessoryDatabaseImpl implements ServerAccessoryDatabase {
    public ServerAccessoryDatabaseImpl() {
    }

    public void setUpdatedCallback(Runnable updatedCallback) {
        this.updatedCallback = updatedCallback;
    }

    public void addAccessory(ServerAccessory accessory) {
        this.accessories.add( accessory );
        if ( this.updatedCallback != null ) {
            this.updatedCallback.run();
        }
    }

    @Override
    public JsonObjectBuilder toJSON() {
        JsonObjectBuilder builder = JsonProvider.provider().createObjectBuilder();
        builder.add("accessories", accessoriesToJSON());
        return builder;
    }

    private JsonArrayBuilder accessoriesToJSON() {
        JsonArrayBuilder builder = JsonProvider.provider().createArrayBuilder();

        this.accessories.forEach(e -> {
            builder.add(e.toJSON());
        });

        return builder;
    }

    private List<ServerAccessory> accessories = new ArrayList<>();

    private Runnable updatedCallback;
}
