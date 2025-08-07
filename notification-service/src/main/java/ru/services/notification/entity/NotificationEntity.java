package ru.services.notification.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.services.notification.model.NotificationType;

import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "notification")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_id_seq")
    @SequenceGenerator(name = "notification_id_seq", sequenceName = "notification_id_seq", allocationSize = 1)
    private Long id;
    @NotBlank
    @Size(min = 1, max = 50)
    private String username;
    @Schema(description = "Contact", example = "alex@example.com")
    @NotBlank(message = "Contact cannot be empty")
    String contact;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    @NotBlank
    private String message;
    private Timestamp created;
}