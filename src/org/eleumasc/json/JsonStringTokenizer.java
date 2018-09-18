package org.eleumasc.json;

import java.io.StringReader;

public class JsonStringTokenizer extends JsonStreamTokenizer {

    public JsonStringTokenizer(String string) {
        super(new StringReader(string));
    }
}
