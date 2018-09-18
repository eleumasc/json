package org.eleumasc.json;

import java.io.IOException;

public interface JsonTokenizer {

    JsonToken nextToken() throws IOException, JsonFormatException;
}
