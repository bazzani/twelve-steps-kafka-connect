package jjug.kafka.connect.smt;

import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.List;
import java.util.Map;

import static org.apache.kafka.connect.data.Schema.INT32_SCHEMA;
import static org.apache.kafka.connect.data.Schema.STRING_SCHEMA;
import static org.assertj.core.api.Assertions.assertThat;

class StructArrayToJsonTransformTest {
    private StructArrayToJsonTransform sut;

    @BeforeEach
    void setup() {
        sut = new StructArrayToJsonTransform();
        sut.configure(Map.of(StructArrayToJsonTransform.CONFIG_ARRAY_FIELD_NAME, "items"));
    }

    @Test
    void shouldApplyTransform() {
        // given
        var arrayStructSchema = SchemaBuilder.struct()
                .field("item_id", INT32_SCHEMA)
                .field("item_name", STRING_SCHEMA)
                .build();
        var valueSchema
                = SchemaBuilder.struct()
                .field("id", INT32_SCHEMA)
                .field("items", SchemaBuilder.array(arrayStructSchema).build())
                .build();
        var value = new Struct(valueSchema)
                .put("id", 1)
                .put("items", List.of(
                        new Struct(arrayStructSchema)
                                .put("item_id", 2)
                                .put("item_name", "test item")
                ));
        var sinkRecord = new SinkRecord("pizza_orders", 1, STRING_SCHEMA, "1", valueSchema, value, 0);

        // when
        var transformedRecord = sut.apply(sinkRecord);

        // then
        var transformedValue = (Struct) transformedRecord.value();

        // check the SMT field is mutated
        assertThat(transformedValue.schema().field("items").schema()).isEqualTo(STRING_SCHEMA);
        JSONAssert.assertEquals("""
                [
                    {
                      "item_id": 2,
                      "item_name": "test item"
                    }
                ]
                """, transformedValue.getString("items"), true);

        // check the other field is not mutated
        assertThat(transformedValue.schema().field("id").schema()).isEqualTo(INT32_SCHEMA);
        assertThat(transformedValue.getInt32("id")).isEqualTo(1);

        System.out.println("transformedValue. = " + transformedValue);

        sut.apply(sinkRecord);
    }
}