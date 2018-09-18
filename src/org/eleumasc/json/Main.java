package org.eleumasc.json;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException, JsonFormatException {
        JsonTokenizer tokenizer = new JsonStreamTokenizer(new FileReader(new File("examples/example.json")));

        JsonParser parser = new JsonBasicParser(tokenizer);

        JsonObject root = parser.parse().asObject();

        for (Map.Entry<String, JsonValue> topic : root.get("quiz").asObject().entrySet()) {
            System.out.println("Domande di " + topic.getKey() + ":");
            for (Map.Entry<String, JsonValue> question : topic.getValue().asObject().entrySet()) {
                System.out.println(question.getValue().asObject().get("question").asString().value());
                System.out.println(question.getValue().asObject().get("answer").asString().value());
            }
            System.out.println();
        }
    }
}
