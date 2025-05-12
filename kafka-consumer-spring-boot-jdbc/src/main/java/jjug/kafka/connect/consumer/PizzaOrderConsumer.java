package jjug.kafka.connect.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Slf4j
@Service
public class PizzaOrderConsumer {
    private static final Random random = new Random();
    private static final ObjectMapper mapper = new ObjectMapper();

    private final JdbcOperations jdbcOperations;

    public PizzaOrderConsumer(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @KafkaListener(topics = "pizza_orders", groupId = "jjug-kafka-connect-spring-boot")
    public void listenForOrders(ConsumerRecord<String, GenericData.Record> kafkaRecord) throws InterruptedException, JsonProcessingException {
        logKafkaRecord(kafkaRecord);

        var pizzaOrder = getPizzaOrderFrom(kafkaRecord);
        sendToKitchen(pizzaOrder);
        napTime();
    }

    private void logKafkaRecord(ConsumerRecord<String, GenericData.Record> kafkaRecord) {
        log.info("kafkaRecord.key           = {}", kafkaRecord.key());
        log.info("           .topic         = {}", kafkaRecord.topic());
        log.info("           .offset        = {}", kafkaRecord.offset());
        log.info("           .partition     = {}", kafkaRecord.partition());
        log.info("           .timestamp     = {}", kafkaRecord.timestamp());
        log.info("           .timestampType = {}", kafkaRecord.timestampType());
        log.info("           .value         = {}", kafkaRecord.value());
    }

    private record PizzaOrder(int storeId, int storeOrderId, int couponCode, LocalDate date, String status,
                              String orderLines) {
    }

    private static PizzaOrder getPizzaOrderFrom(ConsumerRecord<String, GenericData.Record> kafkaRecord) throws JsonProcessingException {
        var orderJsonString = kafkaRecord.value().toString();
        var json = mapper.readTree(orderJsonString);

        var storeId = json.get("store_id").asInt();
        var storeOrderId = json.get("store_order_id").asInt();
        var couponCode = json.get("coupon_code").asInt();

        var dateInDaysFromEpoch = json.get("date").asInt();
        var date = LocalDate.ofEpochDay(dateInDaysFromEpoch);

        var status = json.get("status").toString();
        var orderLines = json.get("order_lines").toString();

        return new PizzaOrder(storeId, storeOrderId, couponCode, date, status, orderLines);
    }

    private void sendToKitchen(PizzaOrder order) {
        log.info("Sending order to kitchen... {}", order);

        var dateAsString = order.date.format(DateTimeFormatter.BASIC_ISO_DATE);
        String sql = """
                INSERT INTO pizza_orders (store_id, store_order_id, coupon_code, date, status, order_lines)
                VALUES (%d, %d, '%s', '%s', '%s', '%s')
                """
                .formatted(order.storeId, order.storeOrderId, order.couponCode,
                        dateAsString, order.status, order.orderLines)
                .trim();
        jdbcOperations.execute(sql);
    }

    private void napTime() throws InterruptedException {
        var napTime = random.nextInt(2_000);

        log.info("I'm tired, going for a nap... for {} ms 🥱\n\n", napTime);

        Thread.sleep(napTime);
    }
}