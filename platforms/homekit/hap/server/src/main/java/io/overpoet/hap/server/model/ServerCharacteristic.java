package io.overpoet.hap.server.model;

import io.overpoet.hap.common.model.Characteristic;
import io.overpoet.hap.common.model.Format;

public interface ServerCharacteristic<JAVA_TYPE, FORMAT_TYPE extends Format<JAVA_TYPE>> extends Characteristic<JAVA_TYPE, FORMAT_TYPE> {
    void updateValue(JAVA_TYPE value);
}
