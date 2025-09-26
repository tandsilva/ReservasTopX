package com.reservastopx.model;

import com.reservastopx.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;

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

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(unique = true, length = 18)
    private String cnpj;

    private String nomeFantasia;
    private String razaoSocial;

    private String telefone;
    private String email;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private int pontos = 0; // Novo campo para pontos

    // Adiciona pontos ao usuário
    public void adicionarPontos(int pontosAdicionados) {
        this.pontos += pontosAdicionados;
    }

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
