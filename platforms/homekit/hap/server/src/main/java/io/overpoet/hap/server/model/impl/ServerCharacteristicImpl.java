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

import io.overpoet.hap.common.model.Characteristic;
import io.overpoet.hap.common.model.CharacteristicType;
import io.overpoet.hap.common.model.Format;
import io.overpoet.hap.common.model.Formats;
import io.overpoet.hap.common.model.Permission;
import io.overpoet.hap.common.model.Service;
import io.overpoet.hap.common.model.impl.AbstractCharacteristicImpl;
import io.overpoet.hap.server.model.ServerCharacteristic;

public class ServerCharacteristicImpl<JAVA_TYPE, FORMAT_TYPE extends Format<JAVA_TYPE>>
        extends AbstractCharacteristicImpl<JAVA_TYPE, FORMAT_TYPE>
        implements ServerCharacteristic<JAVA_TYPE, FORMAT_TYPE> {

    public ServerCharacteristicImpl(Service service, int iid, CharacteristicType type, Consumer<ServerCharacteristicImpl> config) {
        super(service, iid, type);
        config.accept(this);
        //setStoredValue(value);
    }

    @Override
    public void updateValue(JAVA_TYPE value) {
        System.err.println(this + " updateValue " + value + " >> " + this.changeListeners);
        if (value instanceof JsonValue) {
            //value = fromJsonValue((JsonValue) value);
            value = getType().getFormat().fromJSON((JsonValue) value);
        }
        setStoredValue(value);
        for (Consumer<Characteristic<JAVA_TYPE, FORMAT_TYPE>> listener : this.changeListeners) {
            System.err.println("propagate: " + listener);
            listener.accept(this);
        }
    }

    public void requestValueUpdate(JsonValue value) {
        JAVA_TYPE javaValue = getType().getFormat().fromJSON(value);
        if ( ! javaValue.equals(getValue())) {
            requestValueUpdate(getType().getFormat().fromJSON(value));
        }
    }

    @Override
    public void setController(Consumer<JAVA_TYPE> controller) {
        this.controller = controller;
    }

    @Override
    public void requestValueUpdate(JAVA_TYPE value) {
        if (value instanceof JsonValue) {
            value = getType().getFormat().fromJSON((JsonValue) value);
        }
        if (this.controller != null) {
            this.controller.accept(value);
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
            System.err.println( "v=" + getValue());
            System.err.println( "v.json=" + getType().getFormat().toJSON(getValue()));
            builder.add("value", getType().getFormat().toJSON(getValue()));
        }

        return builder;
    }

    public JsonArrayBuilder permissionsToJSON() {
        JsonArrayBuilder builder = JsonProvider.provider().createArrayBuilder();

        for (Permission value : getPermissions()) {
            builder.add(value.toString());
        }

        return builder;
    }

    @Override
    public void addListener(Consumer<Characteristic<JAVA_TYPE, FORMAT_TYPE>> listener) {
        this.changeListeners.add(listener);
        listener.accept(this);
    }

    @Override
    public void removeListener(Consumer<Characteristic<JAVA_TYPE, FORMAT_TYPE>> listener) {
        this.changeListeners.remove(listener);
    }

    @Override
    public void removeAllListeners() {
        this.changeListeners.clear();
    }

    private final Set<Consumer<Characteristic<JAVA_TYPE, FORMAT_TYPE>>> changeListeners = new HashSet<>();

    private Consumer<JAVA_TYPE> controller;
}
