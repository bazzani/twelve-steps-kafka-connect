package jjug.kafka.connect;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerSprint0 {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerSprint0.class);

    public static void main(String[] args) {
        var config = getConfig();

        try (KafkaConsumer<String, Object> consumer = new KafkaConsumer<>(config)) {
            consumer.subscribe(Collections.singletonList("pizza_orders"));

            while (true) {
                ConsumerRecords<String, Object> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, Object> kafkaRecord : records) {
                    log.info("Consumed Kafka record: offset [{}], key [{}], value [{}]]", kafkaRecord.offset(), kafkaRecord.key(), kafkaRecord.value());
                }
            }
        }
    }

    private static Properties getConfig() {
        var config = new Properties();

        config.put("bootstrap.servers", "localhost:9092");
        config.put("auto.offset.reset", "latest");
        config.put("group.id", "jjug-kafka-sprint");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        config.put("schema.registry.url", "http://localhost:8081");

        return config;
    }
}
