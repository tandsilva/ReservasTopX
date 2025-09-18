package com.reservastopx.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Table(name = "restaurants")
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

    private String categoria; // ex: Pizzaria, Japonês etc.

    private LocalDateTime createdAt;
}
