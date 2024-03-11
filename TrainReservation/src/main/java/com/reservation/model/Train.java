package com.reservation.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long trainNo;
    private String trainName;
    private String fromStation;
    private String toStation;
    private Integer seats;
    private Double fare;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "train")
    @ToString.Exclude
    private List<Reservation> reservations;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(Long trainNo) {
		this.trainNo = trainNo;
	}

	public String getTrainName() {
		return trainName;
	}

	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}

	public String getFromStation() {
		return fromStation;
	}

	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
	}

	public String getToStation() {
		return toStation;
	}

	public void setToStation(String toStation) {
		this.toStation = toStation;
	}

	public Integer getSeats() {
		return seats;
	}

	public void setSeats(Integer seats) {
		this.seats = seats;
	}

	public Double getFare() {
		return fare;
	}

	public void setFare(Double fare) {
		this.fare = fare;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	public Train(Long id, Long trainNo, String trainName, String fromStation, String toStation, Integer seats,
			Double fare, List<Reservation> reservations) {
		super();
		this.id = id;
		this.trainNo = trainNo;
		this.trainName = trainName;
		this.fromStation = fromStation;
		this.toStation = toStation;
		this.seats = seats;
		this.fare = fare;
		this.reservations = reservations;
	}

	public Train() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Train [id=" + id + ", trainNo=" + trainNo + ", trainName=" + trainName + ", fromStation=" + fromStation
				+ ", toStation=" + toStation + ", seats=" + seats + ", fare=" + fare + ", reservations=" + reservations
				+ "]";
	}
    
    
}
