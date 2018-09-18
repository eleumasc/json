package org.eleumasc.json;

public class JsonBoolean implements JsonValue<Boolean> {

    private static JsonBoolean trueInstance, falseInstance;
    private boolean value;

    private JsonBoolean(boolean value) {
        this.value = value;
    }

    public static JsonBoolean getInstance(boolean value) {
        JsonBoolean inst = value ? trueInstance : falseInstance;
        if (inst == null) {
            inst = new JsonBoolean(value);
            if (value) {
                trueInstance = inst;
            } else {
                falseInstance = inst;
            }
        }
        return inst;
    }

    @Override
    public Boolean value() {
        return value;
    }

    @Override
    public void accept(JsonVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }
}
