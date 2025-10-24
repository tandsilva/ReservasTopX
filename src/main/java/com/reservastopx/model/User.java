package com.reservastopx.model;

import com.reservastopx.enums.Role;
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

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // 👇 Agora pode ser null (para ADMIN)
    @Column(unique = true, length = 14, nullable = true)
    private String cpf;



    private String nomeFantasia;
    private String razaoSocial;

    private String telefone;
    private String email;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private int pontos = 0;

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
