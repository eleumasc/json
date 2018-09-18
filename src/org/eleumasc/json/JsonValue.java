package org.eleumasc.json;

public interface JsonValue<T> {

    T value();

    void accept(JsonVisitor visitor);

    default JsonString asString() {
        return (JsonString) this;
    }

    default JsonNumber asNumber() {
        return (JsonNumber) this;
    }

    default JsonObject asObject() {
        return (JsonObject) this;
    }

    default JsonArray asArray() {
        return (JsonArray) this;
    }

    default JsonBoolean asBoolean() {
        return (JsonBoolean) this;
    }
}
