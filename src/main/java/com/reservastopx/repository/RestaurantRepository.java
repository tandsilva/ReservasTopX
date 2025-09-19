package com.reservastopx.repository;

import com.reservastopx.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    // Nenhum código aqui dentro, o Spring Data já gera os métodos (findById, save, etc)
}
