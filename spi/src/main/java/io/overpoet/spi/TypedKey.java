package io.overpoet.spi;

public class TypedKey<T> implements Key {

    public static <T> TypedKey<T> of(Class<T> datatype, String...parts) {
        return new TypedKey<>(Key.keyOf(parts), datatype);
    }

    public static <T> TypedKey<T> keyOf(Class<T> datatype, String...parts) {
        return new TypedKey<>(Key.keyOf(parts), datatype);
    }

    public TypedKey(Key key, Class<T> datatype) {
        this.key = key;
        this.datatype = datatype;
    }

    public Key key() {
        return this.key;
    }

    public Class<T> datatype() {
        return this.datatype;
    }

    @Override
    public Key append(String part) {
        return new TypedKey<>(this.key.append(part), this.datatype);
    }

    @Override
    public Key append(String... parts) {
        return new TypedKey<>(this.key.append(parts), this.datatype);
    }

    private final Key key;

    private final Class<T> datatype;
}
