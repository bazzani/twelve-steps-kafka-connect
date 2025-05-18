package jjug.kafka.connect.integration;

import jjug.kafka.connect.jpa.PizzaOrderRepository;
import jjug.kafka.connect.producer.KafkaProducer;
import org.apache.avro.generic.GenericData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.time.OffsetDateTime;
import java.util.List;

import static org.apache.kafka.common.utils.Utils.sleep;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThatList;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class KafkaConnectIntegrationTest {
    private final KafkaProducer producer;
    private final PizzaOrderRepository repository;

    @Autowired
    public KafkaConnectIntegrationTest(KafkaProducer producer, PizzaOrderRepository pizzaOrderRepository) {
        this.producer = producer;
        this.repository = pizzaOrderRepository;
    }

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void shouldSendPizzaOrderToKafkaAndSinkToDatabase() {
        // given
        var testStartTime = OffsetDateTime.now();

        var today = LocalDate.of(2025, Month.MAY, 18);
        var pizzaOrder = createAvroRecord(today);

        // when
        producer.send("pizza_orders", "key", pizzaOrder);
        sleep(2_000);

        // then
        var pizzaOrders = repository.findAllByCreatedAtDbAfter(testStartTime);
        assertThatList(pizzaOrders).hasSize(1);

        var orderFromDB = pizzaOrders.getFirst();
        assertAll(
                () -> assertThat(orderFromDB.getId().getStoreId()).isEqualTo(1)
                , () -> assertThat(orderFromDB.getId().getStoreOrderId()).isEqualTo(2)
                , () -> assertThat(orderFromDB.getCouponCode()).isEqualTo(3)
                , () -> assertThat(orderFromDB.getDate()).isEqualTo(today)
                , () -> assertThat(orderFromDB.getStatus()).isEqualTo("accepted")
                , () -> JSONAssert.assertEquals("""
                                [
                                    {
                                     "product_id": 55,
                                     "category": "calzone",
                                     "quantity": 2,
                                     "unit_price": 3.6,
                                     "net_price": 7.2
                                   }
                                ]
                                """,
                        orderFromDB.getOrderLines(), true)
        );
    }

    private GenericData.Record createAvroRecord(LocalDate date) {
        var schema = PizzaAvroSchema.getPizzaOrderSchema();

        var pizzaRecord = new GenericData.Record(schema);
        pizzaRecord.put("store_id", 1);
        pizzaRecord.put("store_order_id", 2);
        pizzaRecord.put("coupon_code", 3);
        pizzaRecord.put("date", date.toEpochDay());
        pizzaRecord.put("status", "accepted");

        var orderLineArraySchema = schema.getField("order_lines").schema();
        var orderLineSchema = orderLineArraySchema.getElementType();
        var orderLineItem = new GenericData.Record(orderLineSchema);
        orderLineItem.put("product_id", 55);
        orderLineItem.put("category", "calzone");
        orderLineItem.put("quantity", 2);
        orderLineItem.put("unit_price", 3.6);
        orderLineItem.put("net_price", 7.2);

        pizzaRecord.put("order_lines", List.of(orderLineItem));

        return pizzaRecord;
    }
}
