package com.reservation.service;

import com.reservation.model.Train;

import java.util.List;

public interface TrainService {
    public Train addTrain(Train train);
    public boolean checkTrainName(String train_name);
    public boolean checkTrainNo(Long train_no);

    public List<Train> getAllTrains();

    public Train updateTrain(Train train);

    public Train findByTrainNo(Long trainNo);

    public int deleteTrain(Long trainNo);

    public Train findByFromStationToStation(String fromStation, String toStation);
}
