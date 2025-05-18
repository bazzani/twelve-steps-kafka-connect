package jjug.kafka.connect.integration;

import org.apache.avro.Schema;

public class PizzaAvroSchema {
    static Schema getPizzaOrderSchema() {
        return new Schema.Parser().parse("""
                {
                  "type": "record",
                  "name": "pizza_orders",
                  "namespace": "pizza_orders",
                  "fields": [
                    {
                      "name": "store_id",
                      "type": "int"
                    },
                    {
                      "name": "store_order_id",
                      "type": "int"
                    },
                    {
                      "name": "coupon_code",
                      "type": "int"
                    },
                    {
                      "name": "date",
                      "type": {
                        "type": "int",
                        "connect.version": 1,
                        "connect.name": "org.apache.kafka.connect.data.Date",
                        "logicalType": "date"
                      }
                    },
                    {
                      "name": "status",
                      "type": "string"
                    },
                    {
                      "name": "order_lines",
                      "type": {
                        "type": "array",
                        "items": {
                          "type": "record",
                          "name": "order_line",
                          "fields": [
                            {
                              "name": "product_id",
                              "type": "int"
                            },
                            {
                              "name": "category",
                              "type": "string"
                            },
                            {
                              "name": "quantity",
                              "type": "int"
                            },
                            {
                              "name": "unit_price",
                              "type": "double"
                            },
                            {
                              "name": "net_price",
                              "type": "double"
                            }
                          ],
                          "connect.name": "pizza_orders.order_line"
                        }
                      }
                    }
                  ],
                  "connect.name": "pizza_orders.pizza_orders"
                }
                """);
    }
}
