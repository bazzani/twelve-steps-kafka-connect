package jjug.kafka.connect.smt;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Struct;
import org.json.JSONObject;

import java.util.List;
import java.util.StringJoiner;


@Slf4j
public class StructArrayToJsonCreator {
    private StructArrayToJsonCreator() {
    }

    static String createJsonStringArray(List<Struct> structArray) {
        var arrayJoiner = new StringJoiner(", ", "[", "]");

        structArray.stream()
                .map(struct -> {
                    var jsonObject = new JSONObject();
                    struct.schema().fields()
                            .forEach(field -> jsonObject.put(field.name(), struct.get(field.name())));

                    return jsonObject.toString();
                })
                .forEach(arrayJoiner::add);

        var jsonArrayString = arrayJoiner.toString();
        JSONObject.testValidity(jsonArrayString);

        log.info("Created Json from Struct Array: {}", jsonArrayString);

        return jsonArrayString;
    }
}
