package com.reservastopx.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDTO {
    private Long id;
    private Long userId;
    private Long restaurantId;
    private LocalDateTime reservationDate;
    private LocalDateTime createdAt;
}
