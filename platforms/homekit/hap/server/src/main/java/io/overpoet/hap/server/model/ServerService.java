package io.overpoet.hap.server.model;

import java.util.function.Consumer;

import javax.json.JsonObjectBuilder;

import io.overpoet.hap.common.model.CharacteristicType;
import io.overpoet.hap.common.model.Format;
import io.overpoet.hap.common.model.Service;

public interface ServerService extends Service {

    JsonObjectBuilder toJSON();

    <JAVA_TYPE, FORMAT_TYPE extends Format<JAVA_TYPE>>

    void addCharacteristic(int iid,
                           CharacteristicType<JAVA_TYPE, FORMAT_TYPE> type,
                           Consumer<ServerCharacteristic<JAVA_TYPE, FORMAT_TYPE>> config);
}
