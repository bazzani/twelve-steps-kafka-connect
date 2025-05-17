package jjug.kafka.connect.smt;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.List;

class StructArrayToJsonCreatorTest {
    @Test
    void shouldCreateJsonStringArray() {
        // given
        var schema = SchemaBuilder.struct()
                .field("product_id", Schema.INT32_SCHEMA)
                .field("category", Schema.STRING_SCHEMA)
                .field("quantity", Schema.INT32_SCHEMA)
                .field("unit_price", Schema.FLOAT64_SCHEMA)
                .field("net_price", Schema.FLOAT64_SCHEMA)
                .build();
        var item1 = new Struct(schema)
                .put("product_id", 1)
                .put("category", "cat1")
                .put("quantity", 1)
                .put("unit_price", 1.1d)
                .put("net_price", 2.2d);
        var item2 = new Struct(schema)
                .put("product_id", 2)
                .put("category", "cat2")
                .put("quantity", 2)
                .put("unit_price", 2.2d)
                .put("net_price", 3.3d);

        var structArray = List.of(item1, item2);

        // when
        var jsonStringArray = StructArrayToJsonCreator.createJsonStringArray(structArray);

        // then
        JSONAssert.assertEquals("""
                [
                    {
                        "product_id": 1,
                        "category": "cat1",
                        "quantity": 1,
                        "unit_price": 1.1,
                        "net_price": 2.2
                    }
                    ,
                    {
                        "product_id": 2,
                        "category": "cat2",
                        "quantity": 2,
                        "unit_price": 2.2,
                        "net_price": 3.3
                    }
                ]
                """, jsonStringArray, true);
    }
}