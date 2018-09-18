package org.eleumasc.json;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonNumber implements JsonValue<Number> {

    private Number number;

    private String source;

    public JsonNumber(Number number) {
        this.number = number;
    }

    public static JsonNumber parse(String source) throws JsonFormatException {
        final Pattern pattern = Pattern.compile("^(?<sgn>[\\-]?)(?<int>0|[1-9][0-9]*)(\\.(?<frac>[0-9]+))?([eE](?<esgn>[+\\-])?(?<eint>[0-9]+))?$");
        Matcher matcher = pattern.matcher(source);
        if (!matcher.find()) {
            throw new JsonFormatException("Invalid number format: " + source);
        }

        int intPart = Integer.parseInt(matcher.group("int"));
        if ("-".equals(matcher.group("sgn"))) {
            intPart = -intPart;
        }

        boolean existsFracPart = (matcher.group("frac") != null);
        double fracPart = 0;
        if (existsFracPart) {
            fracPart = Integer.parseInt(matcher.group("frac")) / Math.pow(10, matcher.group("frac").length());
        }

        boolean existsExp = (matcher.group("eint") != null);
        int exp = 0;
        if (existsExp) {
            exp = Integer.parseInt("eint");
        }
        if ("-".equals(matcher.group("esgn"))) {
            exp = -exp;
        }

        JsonNumber jsonNumber;
        if (!existsFracPart && exp >= 0) {
            jsonNumber = new JsonNumber(intPart * (int) Math.pow(10, exp));
        } else {
            jsonNumber = new JsonNumber((intPart + fracPart) * Math.pow(10, exp));
        }

        jsonNumber.source = source;

        return jsonNumber;
    }

    @Override
    public Number value() {
        return number;
    }

    @Override
    public void accept(JsonVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        if (source == null) {
            source = number.toString();
        }

        return source;
    }
}
