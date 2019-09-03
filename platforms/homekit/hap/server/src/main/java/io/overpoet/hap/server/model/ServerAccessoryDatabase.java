package io.overpoet.hap.server.model;

import javax.json.JsonObjectBuilder;

import io.overpoet.hap.server.model.impl.ServerCharacteristicImpl;

public interface ServerAccessoryDatabase {
    JsonObjectBuilder toJSON();

    ServerCharacteristicImpl findCharacteristic(int aid, int iid);
}
