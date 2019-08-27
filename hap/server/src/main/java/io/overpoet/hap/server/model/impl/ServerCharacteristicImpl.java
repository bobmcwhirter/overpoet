package io.overpoet.hap.server.model.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.function.Consumer;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

import io.overpoet.hap.common.model.CharacteristicType;
import io.overpoet.hap.common.model.Format;
import io.overpoet.hap.common.model.Permission;
import io.overpoet.hap.common.model.Service;
import io.overpoet.hap.common.model.impl.AbstractCharacteristicImpl;

public class ServerCharacteristicImpl extends AbstractCharacteristicImpl {

    public ServerCharacteristicImpl(Service service, int iid, CharacteristicType type, Consumer<ServerCharacteristicImpl> config) {
        super(service, iid, type);
        config.accept(this);
        //setStoredValue(value);
    }

    @Override
    public void updateValue(Object value) {

    }

    public JsonObjectBuilder toJSON() {
        JsonObjectBuilder builder = JsonProvider.provider().createObjectBuilder();

        builder.add("iid", getIID());
        builder.add("type", getType().getEncodedType());
        builder.add("format", getType().getFormat().toString().toLowerCase());
        builder.add("perms", permissionsToJSON());
        if ( getPermissions().contains(Permission.PAIRED_READ)) {
            if (getType().getFormat() == Format.STRING) {
                builder.add("value", getValue().toString());
            } else if ( getType().getFormat() == Format.FLOAT ) {
                Double v = getValue(Double.class);
                builder.add("value", Math.floor(v * 10) / 10);
            } else {
                builder.add("value", JsonObject.NULL);
            }
        }

        return builder;
    }

    public JsonArrayBuilder permissionsToJSON() {
        JsonArrayBuilder builder = JsonProvider.provider().createArrayBuilder();

        for (Permission value : getPermissions().values()) {
            builder.add(value.toString());
        }

        return builder;
    }
}
