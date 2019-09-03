package io.overpoet.hap.server.model;

import javax.json.JsonObjectBuilder;

import io.overpoet.hap.common.model.Accessory;

public interface ServerAccessory extends Accessory {
    JsonObjectBuilder toJSON();
}
