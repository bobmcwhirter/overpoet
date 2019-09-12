package io.overpoet.hap.server.model;

import java.util.EnumSet;

import javax.json.JsonObjectBuilder;

import io.overpoet.hap.common.model.Characteristic;
import io.overpoet.hap.common.model.Format;
import io.overpoet.hap.common.model.Permission;

public interface ServerCharacteristic<JAVA_TYPE, FORMAT_TYPE extends Format<JAVA_TYPE>> extends Characteristic<JAVA_TYPE, FORMAT_TYPE> {
    void setStoredValue(JAVA_TYPE value);
    void updateValue(JAVA_TYPE value);
    void setPermissions(Permission...permissions);
    JsonObjectBuilder toJSON();
}
