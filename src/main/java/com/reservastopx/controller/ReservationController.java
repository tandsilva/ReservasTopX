package com.reservastopx.controller;
import com.reservastopx.dto.ReservationDTO;
import java.util.List;
import com.reservastopx.model.Restaurant;
import com.reservastopx.model.User;
import com.reservastopx.repository.RestaurantRepository;
import com.reservastopx.repository.UserRepository;
import com.reservastopx.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @PostMapping("/fazer")
    public ResponseEntity<String> fazerReserva(
            @RequestParam Long userId,
            @RequestParam Long restaurantId,
            @RequestParam String reservationDate) {

        // 1️⃣ Validar usuário
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 2️⃣ Validar restaurante
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

        LocalDateTime date;
        try {
            // Formato com dia/mês/ano e hora:minuto
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            date = LocalDateTime.parse(reservationDate, formatter);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Formato de data inválido. Use dd/MM/yyyy HH:mm");
        }

        // 4️⃣ Chamar serviço
        reservationService.fazerReserva(userId, restaurantId, date);

        return ResponseEntity.ok("Reserva criada com sucesso!");
    }
    @GetMapping
    public ResponseEntity<List<ReservationDTO>> listarTodas() {
        List<ReservationDTO> reservas = reservationService.listarTodasReservas();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> buscarPorId(@PathVariable Long id) {
        try {
            ReservationDTO reserva = reservationService.buscarPorId(id);
            return ResponseEntity.ok(reserva);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelarReserva(@PathVariable Long id) {
        try {
            reservationService.cancelarReserva(id);
            return ResponseEntity.ok("Reserva cancelada com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
