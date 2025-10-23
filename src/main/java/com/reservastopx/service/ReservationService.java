package com.reservastopx.service;

import com.reservastopx.dto.ReservationDTO;
import com.reservastopx.enums.StatusReservation;
import com.reservastopx.mapper.ReservationMapper;
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
import java.util.List;
import java.util.stream.Collectors;

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
    @Transactional
    public void cancelarReserva(Long reservationId) {
        Reservation reserva = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        reservationRepository.delete(reserva);
    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> listarTodasReservas() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservationDTO buscarPorId(Long id) {
        Reservation reserva = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
        return ReservationMapper.toDTO(reserva);
    }
    @Transactional
    public void confirmarReserva(Long id) {
        var reserva = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
        reserva.setStatus(StatusReservation.CONFIRMED);
        reservationRepository.save(reserva);
    }
    @Transactional
    public void atualizarStatus(Long id, StatusReservation novoStatus) {
        var reserva = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        // Regras simples pra evitar bagunça
        if (reserva.getStatus() == StatusReservation.CANCELED ||
                reserva.getStatus() == StatusReservation.COMPLETED) {
            throw new RuntimeException("Não é possível alterar uma reserva finalizada ou cancelada.");
        }

        reserva.setStatus(novoStatus);
        reservationRepository.save(reserva);
    }

}
