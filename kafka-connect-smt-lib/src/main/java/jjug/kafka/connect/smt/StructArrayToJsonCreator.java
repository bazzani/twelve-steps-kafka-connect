package jjug.kafka.connect.smt;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Struct;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


@Slf4j
public class StructArrayToJsonCreator {
    private StructArrayToJsonCreator() {
    }

    static String createJsonStringArray(List<Struct> structArray) {
        var jsonArray = new JSONArray();

        structArray.stream()
                .map(struct -> {
                    var jsonObject = new JSONObject();
                    struct.schema().fields()
                            .forEach(field -> jsonObject.put(field.name(), struct.get(field.name())));

                    return jsonObject;
                })
                .forEach(jsonArray::put);

        JSONObject.testValidity(jsonArray);

        log.info("Created Json from Struct Array: {}", jsonArray);

        return jsonArray.toString();
    }
}
