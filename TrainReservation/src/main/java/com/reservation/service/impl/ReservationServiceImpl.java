package com.reservation.service.impl;

import com.reservation.model.Reservation;
import com.reservation.model.User;
import com.reservation.repository.ReservationRepository;
import com.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Override
    public Reservation doReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getReservationByUser(User user) {
        return reservationRepository.findReservationByUser(user);
    }

}
