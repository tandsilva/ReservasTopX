package com.reservastopx.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    private String nomeFantasia;
    private String razaoSocial;
    private String cnpj;
    private String email;
    private String telefone;
    private String endereco;

    private String categoria; // Ex: Pizzaria, Japonês, etc.

    private LocalDateTime createdAt;
}
