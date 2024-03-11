package com.reservation.service.impl;

import com.reservation.model.Train;
import com.reservation.repository.TrainRepository;
import com.reservation.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainServiceImpl implements TrainService {

    @Autowired
    private TrainRepository trainRepository;

    @Override
    public Train addTrain(Train train) {
        return trainRepository.save(train);
    }

    @Override
    public boolean checkTrainName(String train_name) {
        return trainRepository.existsByTrainName(train_name);
    }

    @Override
    public boolean checkTrainNo(Long train_no) {
        return trainRepository.existsByTrainNo(train_no);
    }

    @Override
    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    @Override
    public Train updateTrain(Train train) {
        return trainRepository.save(train);
    }

    @Override
    public Train findByTrainNo(Long trainNo) {
        return trainRepository.findByTrainNo(trainNo);
    }

    @Override
    public int deleteTrain(Long trainNo) {
        return trainRepository.deleteByTrainNo(trainNo);
    }

    @Override
    public Train findByFromStationToStation(String fromStation, String toStation) {
        return trainRepository.findByFromStationToStation(fromStation, toStation);
    }
}
