package org.eleumasc.json;

public interface JsonVisitor {

    void visit(JsonObject object);

    void visit(JsonArray array);

    void visit(JsonNumber number);

    void visit(JsonString string);

    void visit(JsonBoolean value);
}
