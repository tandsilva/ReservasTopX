package com.reservastopx.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "restaurants")
@Getter // Lombok irá gerar os getters automaticamente
@Setter // Lombok irá gerar os setters automaticamente
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private String nomeFantasia;
    private String razaoSocial;
    private String cnpj;
    private String email;
    private String telefone;
    private String endereco;

    private String categoria; // Ex: Pizzaria, Japonês, etc.

    private LocalDateTime createdAt;
}
