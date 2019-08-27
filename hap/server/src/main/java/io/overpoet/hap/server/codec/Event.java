package io.overpoet.hap.server.codec;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

import io.overpoet.hap.server.model.impl.ServerCharacteristicImpl;

public class Event {

    public Event(ServerCharacteristicImpl characteristic) {
        this.characteristic = characteristic;
    }

    public JsonObjectBuilder toJSON() {
        JsonObjectBuilder builder = JsonProvider.provider().createObjectBuilder();
        builder.add( "characteristics", characteristicsToJSON() );
        return builder;
    }

    private JsonArrayBuilder characteristicsToJSON() {
        JsonArrayBuilder builder = JsonProvider.provider().createArrayBuilder();
        builder.add( characteristic.toJSON(true));
        return builder;
    }


    private final ServerCharacteristicImpl characteristic;
}
