package org.eleumasc.json;

import java.util.HashMap;
import java.util.Map;

public class JsonObject extends HashMap<String, JsonValue> implements JsonValue<Map.Entry<String, JsonValue>[]> {

    private Map<String, Map.Entry<String, JsonValue>> members;

    public JsonObject() {
        members = new HashMap<>();
    }

    @Override
    public Map.Entry<String, JsonValue>[] value() {
        return (Map.Entry<String, JsonValue>[]) this.entrySet().toArray();
    }

    @Override
    public void accept(JsonVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Map.Entry<String, JsonValue> member : this.entrySet()) {
            sb.append("\"" + member.getKey() + "\":" + member.getValue() + ",");
        }
        if (this.size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("}");
        return sb.toString();
    }
}
