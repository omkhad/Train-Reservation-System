package com.reservation.repository;

import com.reservation.model.Reservation;
import com.reservation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.user = :user")
    public List<Reservation> findReservationByUser(User user);

}
