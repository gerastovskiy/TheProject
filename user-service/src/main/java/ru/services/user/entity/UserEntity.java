package ru.services.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.services.user.model.UserRole;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    private Long id;
    @NotBlank
    @Size(min = 1, max = 50)
    private String username;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email(message = "Email is invalid")
    @NotBlank
    private String email;
    @NotBlank
    private String phone;
    private Integer telegram;
    @NotBlank
    private String address;
    @Enumerated(EnumType.STRING)
    private UserRole role;
}