package ru.services.delivery.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "delivery")
public class DeliveryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_id_seq")
    @SequenceGenerator(name = "delivery_id_seq", sequenceName = "delivery_id_seq", allocationSize = 1)
    private Long id;
    @NotNull
    @Column(name = "order_id")
    private Long orderId;
    private Timestamp date;
}