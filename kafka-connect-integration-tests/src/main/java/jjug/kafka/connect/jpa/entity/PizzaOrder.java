package jjug.kafka.connect.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "pizza_orders", schema = "public")
public class PizzaOrder {
    @EmbeddedId
    private PizzaOrderId id;

    @ColumnDefault("gen_random_uuid()")
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @ColumnDefault("now()")
    @Column(name = "created_at_db", nullable = false)
    private OffsetDateTime createdAtDb;

    @Column(name = "coupon_code", nullable = false)
    private Integer couponCode;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "status", nullable = false, length = Integer.MAX_VALUE)
    private String status;

    @Column(name = "order_lines", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String orderLines;
}