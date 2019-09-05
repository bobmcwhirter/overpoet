package io.overpoet.lutron.caseta.bridge.protocol;

import javax.json.JsonObject;

public abstract class AbstractRequestMessage {

    abstract public JsonObject toJSON();

}
