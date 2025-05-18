package jjug.kafka.connect.jpa;


import jjug.kafka.connect.jpa.entity.PizzaOrder;
import jjug.kafka.connect.jpa.entity.PizzaOrderId;
import org.springframework.data.repository.CrudRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface PizzaOrderRepository extends CrudRepository<PizzaOrder, PizzaOrderId> {
    List<PizzaOrder> findAllByCreatedAtDbAfter(OffsetDateTime after);
}
