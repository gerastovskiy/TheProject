package ru.services.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "clients")
public class ClientEntity {
    @Id
    @Column(name = "client_id")
    private String clientId;
    private String hash;
}