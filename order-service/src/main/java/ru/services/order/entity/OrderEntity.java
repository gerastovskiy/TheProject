package ru.services.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.services.order.model.NotificationType;
import ru.services.order.model.OrderStatus;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_id_seq")
    @SequenceGenerator(name = "orders_id_seq", sequenceName = "orders_id_seq", allocationSize = 1)
    private Long id;
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(updatable = false, nullable = false, unique = true)
    private UUID uuid;
    @NotBlank
    @Size(min = 1, max = 50)
    private String username;
    @Positive(message = "Order amount must be positive")
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Positive(message = "Product id must be positive")
    @Column(name = "product_id")
    Long productId;
    @Positive(message = "Product quantity must be positive")
    Integer quantity;
    @NotBlank(message = "Contact cannot be empty")
    String contact;
    @Enumerated(EnumType.STRING)
    NotificationType type;
    @CreationTimestamp
    private Timestamp created;
    @UpdateTimestamp
    private Timestamp updated;
}