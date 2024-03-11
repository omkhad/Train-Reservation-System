package com.reservation.service;

import com.reservation.model.Reservation;
import com.reservation.model.Train;
import com.reservation.model.User;

import java.util.List;

public interface ReservationService {
    public Reservation doReservation(Reservation reservation);

    public List<Reservation> getReservationByUser(User user);
}
