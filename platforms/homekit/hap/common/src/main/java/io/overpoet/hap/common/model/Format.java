package io.overpoet.hap.common.model;

import javax.json.JsonValue;

/**
 * Created by bob on 9/14/18.
 */
public abstract class Format<JAVA_TYPE> {

    Format(Class<JAVA_TYPE> datatype, String name) {
        this.datatype = datatype;
        this.name = name;
    }

    public Class<JAVA_TYPE> javaType() {
        return this.datatype;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public abstract JAVA_TYPE fromJSON(JsonValue value);
    public abstract JsonValue toJSON(JAVA_TYPE value);

    private final Class<JAVA_TYPE> datatype;
    private final String name;

    /*
    public static SimpelValue<Bool>

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

     */
}
