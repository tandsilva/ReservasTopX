package com.reservastopx.service;

import com.reservastopx.dto.RestaurantDTO;
import com.reservastopx.mapper.RestaurantMapper;
import com.reservastopx.model.Restaurant;
import com.reservastopx.model.User;
import com.reservastopx.repository.RestaurantRepository;
import com.reservastopx.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    // --- já existente ---
    public RestaurantDTO createRestaurant(RestaurantDTO dto) {
        Restaurant restaurant = RestaurantMapper.toEntity(dto);
        restaurant.setCreatedAt(LocalDateTime.now());
        Restaurant saved = restaurantRepository.save(restaurant);
        return RestaurantMapper.toDTO(saved);
    }

    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantRepository.findAll()
                .stream()
                .map(RestaurantMapper::toDTO)
                .collect(Collectors.toList());
    }

    // --- NOVO: Admin logado cria/atualiza seu restaurante ---
    public RestaurantDTO upsertMyRestaurant(RestaurantDTO dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

        Restaurant restaurant = restaurantRepository.findByUserId(user.getId())
                .orElseGet(Restaurant::new);

        restaurant.setUser(user);
        restaurant.setNomeFantasia(dto.getNomeFantasia());
        restaurant.setRazaoSocial(dto.getRazaoSocial());
        restaurant.setCnpj(dto.getCnpj());
        restaurant.setEmail(dto.getEmail());
        restaurant.setTelefone(dto.getTelefone());
        restaurant.setEndereco(dto.getEndereco());
        restaurant.setCategoria(dto.getCategoria());
        if (restaurant.getCreatedAt() == null) restaurant.setCreatedAt(LocalDateTime.now());

        Restaurant saved = restaurantRepository.save(restaurant);
        return RestaurantMapper.toDTO(saved);
    }

    // --- NOVO: buscar restaurante do admin logado ---
    public RestaurantDTO getMyRestaurant(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

        Restaurant restaurant = restaurantRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));

        return RestaurantMapper.toDTO(restaurant);
    }
}
