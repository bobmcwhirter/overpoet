package io.overpoet.hap.server.model.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;
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
        System.err.println(this + " updateValue " + value + " >> " + this.changeListeners);
        if (value instanceof JsonValue) {
            value = fromJsonValue((JsonValue) value);
        }
        setStoredValue(value);
        for (Consumer<EventableCharacteristic> listener : this.changeListeners) {
            System.err.println("propagate: " + listener);
            listener.accept(this);
        }
    }

    @Override
    public void requestValueUpdate(Object value) {
        if (value instanceof JsonValue) {
            value = fromJsonValue((JsonValue) value);
        }
        for (BiConsumer<EventableCharacteristic, Object> changeRequestListener : this.changeRequestListeners) {
            changeRequestListener.accept(this, value);
        }
    }

    private Object fromJsonValue(JsonValue json) {
        if (getType().getFormat() == Format.STRING) {
            if (json.getValueType() == JsonValue.ValueType.STRING) {
                return ((JsonString) json).getString();
            } else {
                return json.toString();
            }
        }

        if (getType().getFormat() == Format.BOOL) {
            if (json.getValueType() == JsonValue.ValueType.TRUE) {
                return true;
            } else if (json.getValueType() == JsonValue.ValueType.FALSE) {
                return false;
            } else if (json.getValueType() == JsonValue.ValueType.NUMBER) {
                return ((JsonNumber) json).intValue() != 0;
            }
        }

        if (getType().getFormat() == Format.INT) {
            if (json.getValueType() == JsonValue.ValueType.NUMBER) {
                return ((JsonNumber) json).intValue();
            }
        }

        if (getType().getFormat() == Format.FLOAT) {
            if (json.getValueType() == JsonValue.ValueType.NUMBER) {
                return ((JsonNumber) json).doubleValue();
            }
        }

        return null;
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
    public void addChangeListener(Consumer<EventableCharacteristic> listener) {
        this.changeListeners.add(listener);
        listener.accept(this);
    }

    @Override
    public void removeChangeListener(Consumer<EventableCharacteristic> listener) {
        this.changeRequestListeners.remove(listener);
    }

    @Override
    public void removeAllChangeListeners() {
        this.changeRequestListeners.clear();
    }

    @Override
    public void addChangeRequestedListener(BiConsumer<EventableCharacteristic,Object> listener) {
        this.changeRequestListeners.add(listener);
    }

    @Override
    public void removeChangeRequestedListener(BiConsumer<EventableCharacteristic,Object> listener) {
        this.changeListeners.remove(listener);
    }

    @Override
    public void removeAllChangeRequesListeners() {
        this.changeListeners.clear();
    }

    private final Set<BiConsumer<EventableCharacteristic,Object>> changeRequestListeners = new HashSet<>();
    private final Set<Consumer<EventableCharacteristic>> changeListeners = new HashSet<>();
}
