package com.reservastopx.mapper;

import com.reservastopx.dto.UserDTO;
import com.reservastopx.enums.Role;
import com.reservastopx.model.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                // Nunca retornar senha pro front-end
                .password(null)
                .role(user.getRole())
                .cpf(user.getCpf())

                .nomeFantasia(user.getNomeFantasia())
                .razaoSocial(user.getRazaoSocial())
                .telefone(user.getTelefone())
                .email(user.getEmail())
                .pontos(user.getPontos())
                .build();
    }

    public static User toEntity(UserDTO dto) {
        if (dto == null) return null;

        //  Normaliza CPF e CNPJ
        String cpf = clean(dto.getCpf());


        // Define null automático com base na role


        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(dto.getPassword()) // já vem hasheada do service
                .role(dto.getRole())
                .cpf(cpf)

                .nomeFantasia(dto.getNomeFantasia())
                .razaoSocial(dto.getRazaoSocial())
                .telefone(dto.getTelefone())
                .email(dto.getEmail())
                .pontos(dto.getPontos())
                .build();
    }

    private static String clean(String value) {
        return value == null ? null : value.replaceAll("\\D", "");
    }
}
