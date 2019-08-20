package io.overpoet.hap.client.model;

import io.overpoet.hap.client.model.value.BoolValue;
import io.overpoet.hap.client.model.value.DataValue;
import io.overpoet.hap.client.model.value.FloatValue;
import io.overpoet.hap.client.model.value.IntValue;
import io.overpoet.hap.client.model.value.StringValue;
import io.overpoet.hap.client.model.value.TLV8Value;
import io.overpoet.hap.client.model.value.UInt16Value;
import io.overpoet.hap.client.model.value.UInt32Value;
import io.overpoet.hap.client.model.value.UInt64Value;
import io.overpoet.hap.client.model.value.UInt8Value;
import io.overpoet.hap.client.model.value.Value;

/**
 * Created by bob on 9/14/18.
 */
public enum Format {

    BOOL(BoolValue.class),
    UINT8(UInt8Value.class),
    UINT16(UInt16Value.class),
    UINT32(UInt32Value.class),
    UINT64(UInt64Value.class),
    INT(IntValue.class),
    FLOAT(FloatValue.class),
    STRING(StringValue.class),
    TLV8(TLV8Value.class),
    DATA(DataValue.class)
    ;

    Format(Class<? extends Value> valueFormat) {
        this.valueFormat = valueFormat;
    }

    public Class<? extends Value> getValueFormat() {
        return this.valueFormat;
    }

    private final Class<? extends Value> valueFormat;
}
