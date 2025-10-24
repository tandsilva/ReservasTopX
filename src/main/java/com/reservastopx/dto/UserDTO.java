package com.reservastopx.dto;

import com.reservastopx.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User data transfer object")
public class UserDTO {

    @Schema(description = "User ID")
    private Long id;

    @Schema(description = "Username", example = "joao123")
    private String username;

    @Schema(description = "Password", example = "123456")
    private String password;

    @Schema(description = "Role", example = "admin or user")
    private Role role;

    @Schema(description = "CPF", example = "12345678901")
    private String cpf;



    private String nomeFantasia;
    private String razaoSocial;

    private String telefone;
    private String email;

    private int pontos;
}
