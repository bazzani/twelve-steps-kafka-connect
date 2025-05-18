package jjug.kafka.connect.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class PizzaOrderId implements Serializable {
    private static final long serialVersionUID = -3446783174020481802L;

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(name = "store_order_id", nullable = false)
    private Integer storeOrderId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PizzaOrderId entity = (PizzaOrderId) o;

        return Objects.equals(this.storeOrderId, entity.storeOrderId) &&
                Objects.equals(this.storeId, entity.storeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeOrderId, storeId);
    }

}