package com.reservastopx.mapper;


import com.reservastopx.model.Restaurant;
import com.reservastopx.dto.RestaurantDTO;

public class RestaurantMapper {

    public static RestaurantDTO toDTO(Restaurant restaurant) {
        if (restaurant == null) return null;

        return RestaurantDTO.builder()
                .id(restaurant.getId())
                .nomeFantasia(restaurant.getNomeFantasia())
                .razaoSocial(restaurant.getRazaoSocial())
                .cnpj(restaurant.getCnpj())
                .email(restaurant.getEmail())
                .telefone(restaurant.getTelefone())
                .endereco(restaurant.getEndereco())
                .categoria(restaurant.getCategoria())
                .createdAt(restaurant.getCreatedAt())
                .build();
    }

    public static Restaurant toEntity(RestaurantDTO dto) {
        if (dto == null) return null;

        Restaurant restaurant = new Restaurant();
        restaurant.setId(dto.getId());
        restaurant.setNomeFantasia(dto.getNomeFantasia());
        restaurant.setRazaoSocial(dto.getRazaoSocial());
        restaurant.setCnpj(dto.getCnpj());
        restaurant.setEmail(dto.getEmail());
        restaurant.setTelefone(dto.getTelefone());
        restaurant.setEndereco(dto.getEndereco());
        restaurant.setCategoria(dto.getCategoria());
        restaurant.setCreatedAt(dto.getCreatedAt());
        return restaurant;
    }
}
