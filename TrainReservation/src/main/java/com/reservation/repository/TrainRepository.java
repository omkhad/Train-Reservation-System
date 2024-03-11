package com.reservation.repository;

import com.reservation.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TrainRepository extends JpaRepository<Train, Long> {

    public boolean existsByTrainName(String email);
    public boolean existsByTrainNo(Long trainNo);
    @Query("SELECT t FROM Train t WHERE t.trainNo = :trainNo")
    public Train findByTrainNo(Long trainNo);
    @Modifying
    @Transactional
    @Query("DELETE FROM Train t WHERE t.trainNo = :trainNo")
    public int deleteByTrainNo(Long trainNo);

    @Query("SELECT t FROM Train t WHERE t.fromStation = :fromStation and t.toStation = :toStation")
    public Train findByFromStationToStation(String fromStation, String toStation);

}
