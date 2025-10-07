package com.reservastopx.service;

import com.reservastopx.dto.RestaurantDTO;
import com.reservastopx.mapper.RestaurantMapper;
import com.reservastopx.model.Restaurant;
import com.reservastopx.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

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
}
