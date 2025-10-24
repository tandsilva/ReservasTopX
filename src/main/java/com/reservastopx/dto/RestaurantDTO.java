package com.reservastopx.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class RestaurantDTO {
    private Long id;
    private String nomeFantasia;
    private String razaoSocial;
    private String cnpj;
    private String email;
    private String telefone;
    private String endereco;
    private String categoria;
    private LocalDateTime createdAt;

}
