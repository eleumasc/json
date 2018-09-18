package org.eleumasc.json;

public class JsonToken {

    private Type type;

    private JsonValue value;

    public JsonToken(Type type, JsonValue value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public JsonValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(type);
        sb.append(", ");
        sb.append(value);
        sb.append(")");
        return sb.toString();
    }

    public enum Type {
        BEGIN_OBJECT,
        END_OBJECT,
        MEMBERS_SEPARATOR,
        PAIR_SEPARATOR,
        BEGIN_ARRAY,
        END_ARRAY,
        STRING,
        NUMBER,
        BOOLEAN,
        NULL
    }
}
