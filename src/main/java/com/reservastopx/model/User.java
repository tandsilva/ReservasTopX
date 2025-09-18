package com.reservastopx.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome de usuário não precisa ser único
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // "admin", "user", "restaurant_owner" etc.

    // CPF único e obrigatório
    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    // Campos para CNPJ (opcional, usado por restaurantes)
    @Column(unique = true, length = 18)
    private String cnpj;

    private String nomeFantasia; // Nome do restaurante
    private String razaoSocial;  // Nome legal da empresa

    private String telefone;
    private String email;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
