package com.reservastopx.mapper;

import com.reservastopx.dto.UserDTO;
import com.reservastopx.model.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        if(user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .cpf(user.getCpf())
                .cnpj(user.getCnpj())
                .nomeFantasia(user.getNomeFantasia())
                .razaoSocial(user.getRazaoSocial())
                .telefone(user.getTelefone())
                .email(user.getEmail())
                .pontos(user.getPontos())
                .build();
    }

    public static User toEntity(UserDTO dto) {
        if(dto == null) return null;

        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .role(dto.getRole())
                .cpf(dto.getCpf())
                .cnpj(dto.getCnpj())
                .nomeFantasia(dto.getNomeFantasia())
                .razaoSocial(dto.getRazaoSocial())
                .telefone(dto.getTelefone())
                .email(dto.getEmail())
                .pontos(dto.getPontos())
                .build();
    }
}
