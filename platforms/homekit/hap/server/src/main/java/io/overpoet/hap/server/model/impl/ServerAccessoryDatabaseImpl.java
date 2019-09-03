package io.overpoet.hap.server.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

import io.overpoet.hap.common.model.Characteristic;
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

    @Override
    public ServerCharacteristicImpl findCharacteristic(int aid, int iid) {
        Optional<? extends Characteristic> result = this.accessories.stream()
                .filter(e -> e.getAID() == aid)
                .findFirst()
                .flatMap(e -> e.findCharacteristic(iid));
        if ( result.isPresent() ) {
            return (ServerCharacteristicImpl) result.get();
        }

        return null;
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
