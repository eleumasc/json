package org.eleumasc.json;

import org.eleumasc.json.JsonToken.Type;

import java.io.IOException;

public class JsonBasicParser implements JsonParser {

    private JsonTokenizer tokenizer;

    private JsonValue value;

    public JsonBasicParser(JsonTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public JsonValue parse() throws IOException, JsonFormatException {
        if (value == null) {
            value = parseValue(tokenizer.nextToken());
            if (tokenizer.nextToken() != null) {
                throw new JsonFormatException("Multiple values at top level");
            }
        }
        return value;
    }

    private JsonValue parseValue(JsonToken token) throws IOException, JsonFormatException {
        if (token == null) {
            return null;
        }

        Type type = token.getType();
        if (type == Type.STRING ||
                type == Type.NUMBER ||
                type == Type.BOOLEAN ||
                type == Type.NULL) {
            return token.getValue();
        } else if (type == Type.BEGIN_OBJECT) {
            return parseObject();
        } else if (type == Type.BEGIN_ARRAY) {
            return parseArray();
        }

        throw new JsonFormatException("Expected some value token, but token of type " + token.getType() + " found");
    }

    private JsonObject parseObject() throws IOException, JsonFormatException {
        JsonObject object = new JsonObject();

        try {
            JsonToken token = tokenizer.nextToken();
            while (token.getType() != Type.END_OBJECT) {
                if (token.getType() != Type.STRING) {
                    throw new JsonFormatException("Excepted token of type " + Type.STRING + ", but token of type " + token.getType() + " found");
                }
                String key = ((JsonString) token.getValue()).value();
                token = tokenizer.nextToken();
                if (token.getType() != Type.PAIR_SEPARATOR) {
                    throw new JsonFormatException("Excepted token of type " + Type.PAIR_SEPARATOR + ", but token of type " + token.getType() + "found");
                }
                object.put(key, parseValue(tokenizer.nextToken()));
                token = tokenizer.nextToken();
                if (token.getType() == Type.MEMBERS_SEPARATOR) {
                    token = tokenizer.nextToken();
                } else if (token.getType() != Type.END_OBJECT) {
                    throw new JsonFormatException("Expected token of type " + Type.END_OBJECT + " or " + Type.MEMBERS_SEPARATOR + ", but token of type " + token.getType() + " found");
                }
            }
        } catch (NullPointerException ex) {
            throw new JsonFormatException("Expected some token, but end reached");
        }

        return object;
    }

    private JsonArray parseArray() throws IOException, JsonFormatException {
        JsonArray array = new JsonArray();

        try {
            JsonToken token = tokenizer.nextToken();
            while (token.getType() != Type.END_ARRAY) {
                array.add(parseValue(token));
                token = tokenizer.nextToken();
                if (token.getType() == Type.MEMBERS_SEPARATOR) {
                    token = tokenizer.nextToken();
                } else if (token.getType() != Type.END_ARRAY) {
                    throw new JsonFormatException("Expected token of type " + Type.END_ARRAY + " or " + Type.MEMBERS_SEPARATOR + ", but token of type " + token.getType() + " found");
                }
            }
        } catch (NullPointerException ex) {
            throw new JsonFormatException("Expected some token, but end reached");
        }

        return array;
    }
}
