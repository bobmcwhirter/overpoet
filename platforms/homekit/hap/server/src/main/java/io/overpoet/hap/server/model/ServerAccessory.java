package io.overpoet.hap.server.model;

import java.util.function.Consumer;

import javax.json.JsonObjectBuilder;

import io.overpoet.hap.common.model.Accessory;
import io.overpoet.hap.common.model.ServiceType;
import io.overpoet.hap.server.model.impl.ServerServiceImpl;

public interface ServerAccessory extends Accessory {

    void addService(int iid, ServiceType type, Consumer<ServerService> config);

    JsonObjectBuilder toJSON();
}
