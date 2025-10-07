package com.reservastopx.mapper;

import com.reservastopx.dto.ReservationDTO;
import com.reservastopx.model.Reservation;

public class ReservationMapper {

    public static ReservationDTO toDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(reservation.getId());
        dto.setUserId(reservation.getUser().getId());
        dto.setRestaurantId(reservation.getRestaurant().getId());
        dto.setReservationDate(reservation.getReservationDate());
        dto.setCreatedAt(reservation.getCreatedAt());
        return dto;
    }
}
