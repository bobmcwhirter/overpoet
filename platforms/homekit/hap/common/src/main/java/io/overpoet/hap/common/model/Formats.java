package io.overpoet.hap.common.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Base64;

import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64Decoder;
import io.overpoet.hap.common.codec.tlv.TLV;
import io.overpoet.hap.common.model.value.Value;

public interface Formats {
    Format<Boolean> BOOL = new BoolFormat();

    class BoolFormat extends Format<Boolean> {
        BoolFormat() {
            super(Boolean.class, "bool");
        }

        @Override
        public Boolean fromJSON(JsonValue value) {
            ValueType vt = value.getValueType();
            if (vt == ValueType.TRUE) {
                return true;
            }
            if (vt == ValueType.FALSE) {
                return false;
            }
            if (vt == ValueType.NUMBER) {
                return ((JsonNumber) value).intValue() != 0;
            }
            if (vt == ValueType.NULL) {
                return false;
            }
            return false;
        }

        @Override
        public JsonValue toJSON(Boolean value) {
            if ( value == null ) {
                return JsonValue.NULL;
            }
            if (value) {
                return JsonValue.TRUE;
            } else {
                return JsonValue.FALSE;
            }
        }
    }

    Format<Byte> UINT8 = new Uint8Format();

    class Uint8Format extends Format<Byte> {
        Uint8Format() {
            super(Byte.class, "uint8");
        }

        @Override
        public Byte fromJSON(JsonValue value) {
            ValueType vt = value.getValueType();
            if (vt == ValueType.NUMBER) {
                return (byte) ((JsonNumber) value).intValue();
            }
            if (vt == ValueType.TRUE) {
                return 1;
            }
            if (vt == ValueType.FALSE) {
                return 0;
            }
            if (vt == ValueType.NULL) {
                return 0;
            }
            return null;
        }

        @Override
        public JsonValue toJSON(Byte value) {
            if ( value == null ) {
                return JsonValue.NULL;
            }
            return new JsonNumber() {
                @Override
                public boolean isIntegral() {
                    return true;
                }

                @Override
                public int intValue() {
                    return value;
                }

                @Override
                public int intValueExact() {
                    return value;
                }

                @Override
                public long longValue() {
                    return value;
                }

                @Override
                public long longValueExact() {
                    return value;
                }

                @Override
                public BigInteger bigIntegerValue() {
                    return BigInteger.valueOf(value);
                }

                @Override
                public BigInteger bigIntegerValueExact() {
                    return BigInteger.valueOf(value);
                }

                @Override
                public double doubleValue() {
                    return value;
                }

                @Override
                public BigDecimal bigDecimalValue() {
                    return BigDecimal.valueOf(value);
                }

                @Override
                public ValueType getValueType() {
                    return ValueType.NUMBER;
                }

                @Override
                public String toString() {
                    return Byte.toString(value);
                }
            };
        }
    }

    ;

    Format<Short> UINT16 = new Uint16Format();

    class Uint16Format extends Format<Short> {

        Uint16Format() {
            super(Short.class, "uint16");
        }

        @Override
        public Short fromJSON(JsonValue value) {
            ValueType vt = value.getValueType();
            if (vt == ValueType.NUMBER) {
                return (short) ((JsonNumber) value).intValue();
            }
            if (vt == ValueType.TRUE) {
                return 1;
            }
            if (vt == ValueType.FALSE) {
                return 0;
            }
            if (vt == ValueType.NULL) {
                return 0;
            }
            return null;
        }

        @Override
        public JsonValue toJSON(Short value) {
            if ( value == null ) {
                return JsonValue.NULL;
            }
            return new JsonNumber() {
                @Override
                public boolean isIntegral() {
                    return true;
                }

                @Override
                public int intValue() {
                    return value;
                }

                @Override
                public int intValueExact() {
                    return value;
                }

                @Override
                public long longValue() {
                    return value;
                }

                @Override
                public long longValueExact() {
                    return value;
                }

                @Override
                public BigInteger bigIntegerValue() {
                    return BigInteger.valueOf(value);
                }

                @Override
                public BigInteger bigIntegerValueExact() {
                    return BigInteger.valueOf(value);
                }

                @Override
                public double doubleValue() {
                    return value;
                }

                @Override
                public BigDecimal bigDecimalValue() {
                    return BigDecimal.valueOf(value);
                }

                @Override
                public ValueType getValueType() {
                    return ValueType.NUMBER;
                }

                @Override
                public String toString() {
                    return Short.toString(value);
                }
            };
        }
    }

    Format<Integer> UINT32 = new Uint32Format();

    class Uint32Format extends Format<Integer> {

        Uint32Format() {
            super(Integer.class, "uint32");
        }

        @Override
        public Integer fromJSON(JsonValue value) {
            ValueType vt = value.getValueType();
            if ( vt == ValueType.NUMBER ) {
                return ((JsonNumber)value).intValue();
            }
            if ( vt == ValueType.TRUE ) {
                return 1;
            }
            if ( vt == ValueType.FALSE ) {
                return 0;
            }
            if ( vt == ValueType.NULL ) {
                return 0;
            }
            return null;
        }

        @Override
        public JsonValue toJSON(Integer value) {
            if ( value == null ) {
                return JsonValue.NULL;
            }
            return new JsonNumber() {
                @Override
                public boolean isIntegral() {
                    return true;
                }

                @Override
                public int intValue() {
                    return value;
                }

                @Override
                public int intValueExact() {
                    return value;
                }

                @Override
                public long longValue() {
                    return value;
                }

                @Override
                public long longValueExact() {
                    return value;
                }

                @Override
                public BigInteger bigIntegerValue() {
                    return BigInteger.valueOf(value);
                }

                @Override
                public BigInteger bigIntegerValueExact() {
                    return BigInteger.valueOf(value);
                }

                @Override
                public double doubleValue() {
                    return value;
                }

                @Override
                public BigDecimal bigDecimalValue() {
                    return BigDecimal.valueOf(value);
                }

                @Override
                public ValueType getValueType() {
                    return ValueType.NUMBER;
                }

                @Override
                public String toString() {
                    return Integer.toString(value);
                }
            };
        }
    }

    Format<Long> UINT64 = new Uint64Format();

    class Uint64Format extends Format<Long> {
        Uint64Format() {
            super(Long.class, "uint64");
        }

        @Override
        public Long fromJSON(JsonValue value) {
            ValueType vt = value.getValueType();
            if ( vt == ValueType.NUMBER ) {
                return ((JsonNumber)value).longValue();
            }
            if ( vt == ValueType.TRUE ) {
                return 1L;
            }
            if ( vt == ValueType.FALSE ) {
                return 0L;
            }
            if ( vt == ValueType.NULL ) {
                return 0L;
            }
            return null;
        }

        @Override
        public JsonValue toJSON(Long value) {
            if ( value == null ) {
                return JsonValue.NULL;
            }
            return new JsonNumber() {
                @Override
                public boolean isIntegral() {
                    return true;
                }

                @Override
                public int intValue() {
                    return value.intValue();
                }

                @Override
                public int intValueExact() {
                    return value.intValue();
                }

                @Override
                public long longValue() {
                    return value;
                }

                @Override
                public long longValueExact() {
                    return value;
                }

                @Override
                public BigInteger bigIntegerValue() {
                    return BigInteger.valueOf(value);
                }

                @Override
                public BigInteger bigIntegerValueExact() {
                    return BigInteger.valueOf(value);
                }

                @Override
                public double doubleValue() {
                    return value.doubleValue();
                }

                @Override
                public BigDecimal bigDecimalValue() {
                    return BigDecimal.valueOf(value);
                }

                @Override
                public ValueType getValueType() {
                    return ValueType.NUMBER;
                }

                @Override
                public String toString() {
                    return Long.toString(value);
                }
            };
        }
    }

    Format<Integer> INT = new IntFormat();

    class IntFormat extends Format<Integer> {
        IntFormat() {
            super(Integer.class, "int");
        }

        @Override
        public Integer fromJSON(JsonValue value) {
            ValueType vt = value.getValueType();
            if ( vt == ValueType.NUMBER ) {
                return ((JsonNumber)value).intValue();
            }
            if ( vt == ValueType.TRUE ) {
                return 1;
            }
            if ( vt == ValueType.FALSE ) {
                return 0;
            }
            return null;
        }

        @Override
        public JsonValue toJSON(Integer value) {
            if ( value == null ) {
                return JsonValue.NULL;
            }
            return new JsonNumber() {
                @Override
                public boolean isIntegral() {
                    return true;
                }

                @Override
                public int intValue() {
                    return value;
                }

                @Override
                public int intValueExact() {
                    return value;
                }

                @Override
                public long longValue() {
                    return value;
                }

                @Override
                public long longValueExact() {
                    return value;
                }

                @Override
                public BigInteger bigIntegerValue() {
                    return BigInteger.valueOf(value);
                }

                @Override
                public BigInteger bigIntegerValueExact() {
                    return BigInteger.valueOf(value);
                }

                @Override
                public double doubleValue() {
                    return value;
                }

                @Override
                public BigDecimal bigDecimalValue() {
                    return BigDecimal.valueOf(value);
                }

                @Override
                public ValueType getValueType() {
                    return ValueType.NUMBER;
                }

                @Override
                public String toString() {
                    return Integer.toString(value);
                }
            };
        }
    }


    Format<Double> FLOAT = new FloatFormat();

    class FloatFormat extends Format<Double> {
        FloatFormat() {
            super(Double.class, "float");
        }

        @Override
        public Double fromJSON(JsonValue value) {
            ValueType vt = value.getValueType();
            if ( vt == ValueType.NUMBER ) {
                return ((JsonNumber)value).doubleValue();
            }
            return null;
        }

        @Override
        public JsonValue toJSON(Double value) {
            if ( value == null )  {
                return JsonValue.NULL;
            }

            return new JsonNumber() {
                @Override
                public boolean isIntegral() {
                    return false;
                }

                @Override
                public int intValue() {
                    return value.intValue();
                }

                @Override
                public int intValueExact() {
                    return value.intValue();
                }

                @Override
                public long longValue() {
                    return value.longValue();
                }

                @Override
                public long longValueExact() {
                    return value.longValue();
                }

                @Override
                public BigInteger bigIntegerValue() {
                    return BigInteger.valueOf( value.longValue());
                }

                @Override
                public BigInteger bigIntegerValueExact() {
                    return BigInteger.valueOf(value.longValue());
                }

                @Override
                public double doubleValue() {
                    return value;
                }

                @Override
                public BigDecimal bigDecimalValue() {
                    return BigDecimal.valueOf(value);
                }

                @Override
                public ValueType getValueType() {
                    return ValueType.NUMBER;
                }

                @Override
                public String toString() {
                    return Double.toString(value);
                }
            };
        }
    }

    Format<String> STRING = new StringFormat();

    class StringFormat extends Format<String> {
        StringFormat() {
            super(String.class, "string");
        }

        @Override
        public String fromJSON(JsonValue value) {
            return value.toString();
        }

        @Override
        public JsonValue toJSON(String value) {
            if ( value == null ) {
                return JsonValue.NULL;
            }
            return new JsonString() {
                @Override
                public String getString() {
                    return value;
                }

                @Override
                public CharSequence getChars() {
                    return value;
                }

                @Override
                public ValueType getValueType() {
                    return ValueType.STRING;
                }

                @Override
                public String toString() {
                    //Borrowed from glassfish

                    StringBuilder sb = new StringBuilder();
                    sb.append('"');

                    for(int i = 0; i < value.length(); i++) {
                        char c = value.charAt(i);
                        // unescaped = %x20-21 | %x23-5B | %x5D-10FFFF
                        if (c >= 0x20 && c <= 0x10ffff && c != 0x22 && c != 0x5c) {
                            sb.append(c);
                        } else {
                            switch (c) {
                                case '"':
                                case '\\':
                                    sb.append('\\'); sb.append(c);
                                    break;
                                case '\b':
                                    sb.append('\\'); sb.append('b');
                                    break;
                                case '\f':
                                    sb.append('\\'); sb.append('f');
                                    break;
                                case '\n':
                                    sb.append('\\'); sb.append('n');
                                    break;
                                case '\r':
                                    sb.append('\\'); sb.append('r');
                                    break;
                                case '\t':
                                    sb.append('\\'); sb.append('t');
                                    break;
                                default:
                                    String hex = "000" + Integer.toHexString(c);
                                    sb.append("\\u").append(hex.substring(hex.length() - 4));
                            }
                        }
                    }

                    sb.append('"');
                    return sb.toString();
                }
            };
        }

    }


    Format<TLV> TLV8 = new TLV8Format();

    class TLV8Format extends Format<TLV> {
        TLV8Format() {
            super(TLV.class, "tlv8");
        }

        @Override
        public TLV fromJSON(JsonValue value) {
            ValueType vt = value.getValueType();
            if ( vt == ValueType.STRING ) {
                String str = ((JsonString) value).getString();
                byte[] decoded = Base64.getDecoder().decode(str);
                return TLV.decodeFrom(decoded);
            }
            return null;
        }

        @Override
        public JsonValue toJSON(TLV value) {
            ByteBuf buf = Unpooled.buffer();
            value.encodeInto(buf);
            String str = buf.toString(Charset.forName("UTF8"));
            return new JsonString() {
                @Override
                public String getString() {
                    return str;
                }

                @Override
                public CharSequence getChars() {
                    return str;
                }

                @Override
                public ValueType getValueType() {
                    return ValueType.STRING;
                }

                @Override
                public String toString() {
                    //Borrowed from glassfish

                    StringBuilder sb = new StringBuilder();
                    sb.append('"');

                    for(int i = 0; i < str.length(); i++) {
                        char c = str.charAt(i);
                        // unescaped = %x20-21 | %x23-5B | %x5D-10FFFF
                        if (c >= 0x20 && c <= 0x10ffff && c != 0x22 && c != 0x5c) {
                            sb.append(c);
                        } else {
                            switch (c) {
                                case '"':
                                case '\\':
                                    sb.append('\\'); sb.append(c);
                                    break;
                                case '\b':
                                    sb.append('\\'); sb.append('b');
                                    break;
                                case '\f':
                                    sb.append('\\'); sb.append('f');
                                    break;
                                case '\n':
                                    sb.append('\\'); sb.append('n');
                                    break;
                                case '\r':
                                    sb.append('\\'); sb.append('r');
                                    break;
                                case '\t':
                                    sb.append('\\'); sb.append('t');
                                    break;
                                default:
                                    String hex = "000" + Integer.toHexString(c);
                                    sb.append("\\u").append(hex.substring(hex.length() - 4));
                            }
                        }
                    }

                    sb.append('"');
                    return sb.toString();
                }
            };
        }
    }
}
