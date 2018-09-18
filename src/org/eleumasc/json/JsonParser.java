package org.eleumasc.json;

import java.io.IOException;

public interface JsonParser {

    JsonValue parse() throws IOException, JsonFormatException;
}
