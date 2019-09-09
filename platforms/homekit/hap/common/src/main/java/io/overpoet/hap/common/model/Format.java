package io.overpoet.hap.common.model;

import java.util.EnumSet;

import io.overpoet.hap.common.model.value.BoolValue;
import io.overpoet.hap.common.model.value.DataValue;
import io.overpoet.hap.common.model.value.FloatValue;
import io.overpoet.hap.common.model.value.IntValue;
import io.overpoet.hap.common.model.value.StringValue;
import io.overpoet.hap.common.model.value.TLV8Value;
import io.overpoet.hap.common.model.value.UInt16Value;
import io.overpoet.hap.common.model.value.UInt32Value;
import io.overpoet.hap.common.model.value.UInt64Value;
import io.overpoet.hap.common.model.value.UInt8Value;
import io.overpoet.hap.common.model.value.Value;

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
    DATA(DataValue.class);

    public static EnumSet<Format> INTEGRAL = EnumSet.of(INT, UINT8, UINT16, UINT32, UINT64);

    Format(Class<? extends Value> valueFormat) {
        this.valueFormat = valueFormat;
    }

    public Class<? extends Value> getValueFormat() {
        return this.valueFormat;
    }

    private final Class<? extends Value> valueFormat;
}
