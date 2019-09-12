package io.overpoet.hap.server.model.impl;

import java.util.function.Consumer;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

import io.overpoet.hap.common.model.Accessory;
import io.overpoet.hap.common.model.CharacteristicType;
import io.overpoet.hap.common.model.Format;
import io.overpoet.hap.common.model.ServiceType;
import io.overpoet.hap.common.model.impl.AbstractServiceImpl;

public class ServerServiceImpl extends AbstractServiceImpl<ServerCharacteristicImpl> {
    public ServerServiceImpl(Accessory accessory, int iid, ServiceType type, Consumer<ServerServiceImpl> config) {
        super(accessory, iid, type);
        config.accept(this);
    }

    public <JAVA_TYPE, FORMAT_TYPE extends Format<JAVA_TYPE>>
    void addCharacteristic(int iid,
                           CharacteristicType<JAVA_TYPE, FORMAT_TYPE> type,
                           Consumer<ServerCharacteristicImpl<JAVA_TYPE, FORMAT_TYPE>> config) {
        addCharacteristic(new ServerCharacteristicImpl(this, iid, type, config));
    }

    public JsonObjectBuilder toJSON() {
        JsonObjectBuilder builder = JsonProvider.provider().createObjectBuilder();
        builder.add("type", getType().getEncodedType());
        builder.add("iid", getIID());
        builder.add("characteristics", characteristicsToJSON());
        return builder;
    }

    public JsonArrayBuilder characteristicsToJSON() {
        JsonArrayBuilder builder = JsonProvider.provider().createArrayBuilder();

        for (ServerCharacteristicImpl characteristic : getCharacteristics()) {
            builder.add(characteristic.toJSON());
        }

        return builder;
    }

}
