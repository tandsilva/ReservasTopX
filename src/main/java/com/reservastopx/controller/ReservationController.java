package com.reservastopx.controller;

import com.reservastopx.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/reservas")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/fazer")
    public void fazerReserva(
            @RequestParam Long userId,
            @RequestParam Long restaurantId,
            @RequestParam String reservationDate) {

        // Definindo um formato personalizado para a data (se necessário)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Ex: 2025-09-19 14:30:00

        // Converte a string para LocalDateTime usando o formato especificado
        LocalDateTime date = LocalDateTime.parse(reservationDate, formatter);

        // Chama o serviço para fazer a reserva e adicionar pontos
        reservationService.fazerReserva(userId, restaurantId,date);
    }
}
