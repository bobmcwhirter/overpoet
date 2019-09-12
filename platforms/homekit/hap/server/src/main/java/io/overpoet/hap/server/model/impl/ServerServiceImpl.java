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
import io.overpoet.hap.server.model.ServerCharacteristic;
import io.overpoet.hap.server.model.ServerService;

public class ServerServiceImpl extends AbstractServiceImpl<ServerCharacteristic> implements ServerService {
    public ServerServiceImpl(Accessory accessory, int iid, ServiceType type, Consumer<ServerService> config) {
        super(accessory, iid, type);
        config.accept(this);
    }

    public <JAVA_TYPE, FORMAT_TYPE extends Format<JAVA_TYPE>>
    void addCharacteristic(int iid,
                           CharacteristicType<JAVA_TYPE, FORMAT_TYPE> type,
                           Consumer<ServerCharacteristic<JAVA_TYPE, FORMAT_TYPE>> config) {
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

        for (ServerCharacteristic characteristic : getCharacteristics()) {
            builder.add(characteristic.toJSON());
        }

        return builder;
    }

}
