package org.eleumasc.json;

import org.eleumasc.json.JsonToken.Type;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class JsonStreamTokenizer implements JsonTokenizer {

    private BufferedReader reader;

    public JsonStreamTokenizer(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    @Override
    public JsonToken nextToken() throws IOException, JsonFormatException {
        skipWhitespaces();

        int input = reader.read();

        if (input == -1) {
            return null;
        }

        switch (input) {
            case '"':
                return nextString();
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return nextNumber();
            case '{':
                return new JsonToken(Type.BEGIN_OBJECT, null);
            case '}':
                return new JsonToken(Type.END_OBJECT, null);
            case ',':
                return new JsonToken(Type.MEMBERS_SEPARATOR, null);
            case ':':
                return new JsonToken(Type.PAIR_SEPARATOR, null);
            case '[':
                return new JsonToken(Type.BEGIN_ARRAY, null);
            case ']':
                return new JsonToken(Type.END_ARRAY, null);
            case 't':
            case 'f':
            case 'n':
                return nextSpecialValue();
        }

        throw new JsonFormatException("Invalid token: " + input);
    }

    private void skipWhitespaces() throws IOException {
        int input;

        do {
            reader.mark(1);
            input = reader.read();
        } while (input != -1 && Character.isWhitespace(input));

        reader.reset();
    }

    private JsonToken nextString() throws IOException, JsonFormatException {
        StringBuilder sb = new StringBuilder("\"");

        reader.reset();

        int input = reader.read();
        if (input != '\"') {
            throw new JsonFormatException("Expected \", but " + ((char) input) + " found");
        }

        boolean escape = false;
        input = reader.read();
        while (input != -1 && (escape || input != '\"')) {
            if (escape) {
                escape = false;
            } else if (input == '\\') {
                escape = true;
            }
            sb.append((char) input);
            input = reader.read();
        }

        reader.mark(1);

        if (input == -1) {
            throw new IOException("Expected \", but end reached");
        }

        sb.append("\"");

        String source = sb.toString();
        try {
            return new JsonToken(JsonToken.Type.STRING, JsonString.parse(source));
        } catch (JsonFormatException ex) {
            throw new JsonFormatException("Invalid string token: " + source);
        }
    }

    private JsonToken nextNumber() throws IOException, JsonFormatException {
        StringBuilder sb = new StringBuilder();

        reader.reset();

        int input = reader.read();
        while (Character.isDigit(input) ||
                input == '+' ||
                input == '-' ||
                input == '.' ||
                input == 'e' ||
                input == 'E') {
            sb.append((char) input);
            reader.mark(1);
            input = reader.read();
        }

        reader.reset();

        String source = sb.toString();
        try {
            return new JsonToken(Type.NUMBER, JsonNumber.parse(source));
        } catch (JsonFormatException ex) {
            throw new JsonFormatException("Invalid number token: " + source);
        }
    }

    private JsonToken nextSpecialValue() throws IOException, JsonFormatException {
        StringBuilder sb = new StringBuilder();

        reader.reset();

        int input = reader.read();
        while (Character.isAlphabetic(input)) {
            sb.append((char) input);
            reader.mark(1);
            input = reader.read();
        }

        reader.reset();

        String source = sb.toString();
        if (source.equals("true")) {
            return new JsonToken(Type.BOOLEAN, JsonBoolean.getInstance(true));
        } else if (source.equals("false")) {
            return new JsonToken(Type.BOOLEAN, JsonBoolean.getInstance(false));
        } else if (source.equals("null")) {
            return new JsonToken(Type.NULL, null);
        }

        throw new JsonFormatException("Invalid special value token: " + source);
    }
}
