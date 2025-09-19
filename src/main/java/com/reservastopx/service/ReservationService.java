package com.reservastopx.service;

import com.reservastopx.model.Reservation;
import com.reservastopx.model.Restaurant;
import com.reservastopx.model.User;
import com.reservastopx.repository.ReservationRepository;
import com.reservastopx.repository.RestaurantRepository;
import com.reservastopx.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public void fazerReserva(Long userId, Long restaurantId, LocalDateTime reservationDate) {
        // Buscar usuário
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Buscar restaurante
        //erro por falh ano service
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

        // Criar reserva
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRestaurant(restaurant);
        reservation.setReservationDate(reservationDate);

        // Salvar a reserva
        reservationRepository.save(reservation);

        // Adicionar pontos ao usuário
        user.adicionarPontos(1);
        userRepository.save(user);
    }
}
