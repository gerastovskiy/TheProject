package ru.services.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
    @SequenceGenerator(name = "product_id_seq", sequenceName = "product_id_seq", allocationSize = 1)
    private Long id;
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(updatable = false, nullable = false, unique = true)
    private UUID uuid;
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
    @PositiveOrZero(message = "Quantity must be positive or zero")
    private Integer quantity;
    @PositiveOrZero(message = "Price must be positive or zero")
    private Double price;
    @BooleanFlag
    private Boolean active;
    private String description;
    private Timestamp created;
    private Timestamp updated;
}