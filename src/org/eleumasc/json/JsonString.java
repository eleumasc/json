package org.eleumasc.json;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonString implements JsonValue<String> {

    private String string;

    private String source;

    public JsonString(String string) {
        this.string = string;
    }

    public static JsonString parse(String source) throws JsonFormatException {
        final Pattern pattern = Pattern.compile("^\"([^\"\\\\]+|\\\\([\"\\\\/bfnrt]|u[A-Fa-f0-9]{4}))*\"$");
        Matcher matcher = pattern.matcher(source);
        if (!matcher.find()) {
            throw new JsonFormatException("Invalid string format: " + source);
        }

        String chars = source.substring(1, source.length() - 1);

        StringBuilder sb = new StringBuilder();
        boolean escape = false;
        int i = 0;
        while (i < chars.length()) {
            if (!escape) {
                if (chars.charAt(i) != '\\') {
                    sb.append(chars.charAt(i));
                } else {
                    escape = true;
                }
                i++;
            } else {
                char escapeChar = chars.charAt(i++);
                switch (escapeChar) {
                    case '"':
                        sb.append('"');
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    case '/':
                        sb.append('/');
                        break;
                    case 'b':
                        sb.append('\b');
                        break;
                    case 'f':
                        sb.append('\f');
                        break;
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    case 'u': {
                        String hexValue = chars.substring(i, i + 4);
                        sb.append((char) Integer.parseInt(hexValue, 16));
                        i += 4;
                        break;
                    }
                    default:
                        throw new JsonFormatException("Invalid escape character: " + escapeChar);
                }
                escape = false;
            }
        }

        JsonString jsonString = new JsonString(sb.toString());

        jsonString.source = source;

        return jsonString;
    }

    @Override
    public String value() {
        return string;
    }

    @Override
    public void accept(JsonVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        if (source == null) {
            StringBuilder sb = new StringBuilder("\"");

            for (int i = 0; i < string.length(); i++) {
                char iChar = string.charAt(i++);
                switch (iChar) {
                    case '"':
                        sb.append("\\\"");
                        break;
                    case '\\':
                        sb.append("\\\\");
                        break;
                    case '/':
                        sb.append("\\/");
                        break;
                    case '\b':
                        sb.append("\\\b");
                        break;
                    case '\f':
                        sb.append("\\\f");
                        break;
                    case '\n':
                        sb.append("\\\n");
                        break;
                    case '\r':
                        sb.append("\\\r");
                        break;
                    case '\t':
                        sb.append("\\\t");
                    default:
                        sb.append(iChar);
                }
            }

            sb.append("\"");

            source = sb.toString();
        }

        return source;
    }
}
