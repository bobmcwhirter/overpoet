package io.overpoet.hap.server.model.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

import io.overpoet.hap.common.model.CharacteristicType;
import io.overpoet.hap.common.model.EventableCharacteristic;
import io.overpoet.hap.common.model.Format;
import io.overpoet.hap.common.model.Permission;
import io.overpoet.hap.common.model.Service;
import io.overpoet.hap.common.model.impl.AbstractCharacteristicImpl;

public class ServerCharacteristicImpl extends AbstractCharacteristicImpl implements EventableCharacteristic {

    public ServerCharacteristicImpl(Service service, int iid, CharacteristicType type, Consumer<ServerCharacteristicImpl> config) {
        super(service, iid, type);
        config.accept(this);
        //setStoredValue(value);
    }

    @Override
    public void updateValue(Object value) {
        setStoredValue(value);
        for (Consumer<EventableCharacteristic> listener : this.listeners) {
            listener.accept(this);
        }
    }

    public JsonObjectBuilder toJSON() {
        return toJSON(false);
    }

    public JsonObjectBuilder toJSON(boolean simplified) {
        JsonObjectBuilder builder = JsonProvider.provider().createObjectBuilder();

        if (simplified) {
            builder.add("aid", getService().getAccessory().getAID());
        }
        builder.add("iid", getIID());
        if (!simplified) {
            builder.add("type", getType().getEncodedType());
            builder.add("format", getType().getFormat().toString().toLowerCase());
            builder.add("perms", permissionsToJSON());
        }
        if (getPermissions().contains(Permission.PAIRED_READ)) {
            if (getType().getFormat() == Format.STRING) {
                builder.add("value", getValue().toString());
            } else if (getType().getFormat() == Format.FLOAT) {
                Double v = getValue(Double.class);
                builder.add("value", Math.floor(v * 10) / 10);
            } else if (Format.INTEGRAL.contains(getType().getFormat())) {
                Integer v = getValue(Integer.class);
                builder.add("value", v);
            } else if (getType().getFormat() == Format.BOOL) {
                Boolean v = getValue(Boolean.class);
                builder.add("value", v);
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

    @Override
    public void addListener(Consumer<EventableCharacteristic> listener) {
        this.listeners.add(listener);
        listener.accept(this);
    }

    @Override
    public void removeListener(Consumer<EventableCharacteristic> listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void removeAllListeners() {
        this.listeners.clear();
    }

    private final Set<Consumer<EventableCharacteristic>> listeners = new HashSet<>();
}
