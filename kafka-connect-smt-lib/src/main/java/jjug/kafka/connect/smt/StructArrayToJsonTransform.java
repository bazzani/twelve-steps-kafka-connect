package jjug.kafka.connect.smt;

import org.apache.kafka.common.cache.Cache;
import org.apache.kafka.common.cache.LRUCache;
import org.apache.kafka.common.cache.SynchronizedCache;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.transforms.Transformation;
import org.apache.kafka.connect.transforms.util.Requirements;
import org.apache.kafka.connect.transforms.util.SchemaUtil;
import org.apache.kafka.connect.transforms.util.SimpleConfig;

import java.util.List;
import java.util.Map;

public class StructArrayToJsonTransform implements Transformation<SinkRecord> {
    static final String CONFIG_ARRAY_FIELD_NAME = "arrayField";

    static final ConfigDef CONFIG_DEF = new ConfigDef()
            .define(CONFIG_ARRAY_FIELD_NAME,
                    ConfigDef.Type.STRING,
                    ConfigDef.NO_DEFAULT_VALUE,
                    ConfigDef.CompositeValidator.of(
                            new ConfigDef.NonNullValidator(),
                            new ConfigDef.NonEmptyString()
                    ),
                    ConfigDef.Importance.HIGH,
                    "The field in the record with the Struct array");

    private Cache<Schema, Schema> schemaCache;
    private String arrayFieldName;

    @Override
    public void configure(Map<String, ?> configs) {
        var config = new SimpleConfig(CONFIG_DEF, configs);
        arrayFieldName = config.getString(CONFIG_ARRAY_FIELD_NAME);
        schemaCache = new SynchronizedCache<>(new LRUCache<>(16));
    }

    @Override
    public ConfigDef config() {
        return CONFIG_DEF;
    }

    @Override
    public void close() {
        schemaCache = null;
    }

    @Override
    public SinkRecord apply(SinkRecord record) {
        if (record.valueSchema() == null) {
            throw new RuntimeException("StructArrayToJsonTransform requires a value schema");
        }

        return applyWithSchema(record);
    }

    private SinkRecord applyWithSchema(SinkRecord sinkRecord) {
        var value = Requirements.requireStruct(sinkRecord.value(), "converting an array of Struct objects to a JSON string");

        var updatedSchema = getUpdatedSchema(value.schema());
        var updatedValue = getUpdatedValue(value, updatedSchema);

        return newRecord(sinkRecord, updatedSchema, updatedValue);
    }

    private Schema getUpdatedSchema(Schema originalSchema) {
        var updatedSchema = schemaCache.get(originalSchema);

        if (updatedSchema == null) {
            updatedSchema = makeUpdatedSchema(originalSchema);
            schemaCache.put(originalSchema, updatedSchema);
        }

        return updatedSchema;
    }

    private Schema makeUpdatedSchema(Schema schema) {
        validateArrayFieldIsStructArray(schema);

        var schemaBuilder = SchemaUtil.copySchemaBasics(schema);

        schema.fields().stream()
                .filter(field -> !field.name().equals(arrayFieldName))
                .forEach(field -> schemaBuilder.field(field.name(), field.schema()));

        schemaBuilder.field(arrayFieldName, Schema.STRING_SCHEMA);

        return schemaBuilder.build();
    }

    private void validateArrayFieldIsStructArray(Schema schema) {
        var structArraySchema = SchemaBuilder.array(SchemaBuilder.struct().build()).build();

        if (schema.field(arrayFieldName).schema().equals(structArraySchema)) {
            throw new RuntimeException("StructArrayToJsonTransform arrayField should be a Struct Array");
        }
    }

    private Struct getUpdatedValue(Struct originalValue, Schema updatedSchema) {
        var updatedValue = new Struct(updatedSchema);

        applyOtherFields(originalValue, updatedValue);
        applyJsonField(originalValue, updatedValue);

        return updatedValue;
    }

    private void applyOtherFields(Struct originalValue, Struct updatedValue) {
        originalValue.schema().fields().stream()
                .filter(field -> !field.name().equals(arrayFieldName))
                .forEach(field -> updatedValue.put(field.name(), originalValue.get(field.name())));
    }

    private void applyJsonField(Struct originalValue, Struct updatedValue) {
        List<Struct> structArray = originalValue.getArray(arrayFieldName);
        var jsonStringArray = StructArrayToJsonCreator.createJsonStringArray(structArray);

        updatedValue.put(arrayFieldName, jsonStringArray);
    }

    private SinkRecord newRecord(SinkRecord oldRecord, Schema updatedSchema, Struct updatedValue) {
        return oldRecord.newRecord(
                oldRecord.topic(),
                oldRecord.kafkaPartition(),
                oldRecord.keySchema(), oldRecord.key(),
                updatedSchema, updatedValue,
                oldRecord.timestamp()
        );
    }
}
