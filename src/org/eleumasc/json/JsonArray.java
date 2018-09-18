package org.eleumasc.json;

import java.util.ArrayList;

public class JsonArray extends ArrayList<JsonValue> implements JsonValue<JsonValue[]> {

    public JsonArray(JsonValue... elements) {
        for (JsonValue element : elements) {
            this.add(element);
        }
    }

    @Override
    public JsonValue[] value() {
        return (JsonValue[]) this.toArray();
    }

    @Override
    public void accept(JsonVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (JsonValue element : this) {
            sb.append(element.toString() + ",");
        }
        if (this.size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }
}
