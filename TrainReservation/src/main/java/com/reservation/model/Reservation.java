package com.reservation.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String reservationDate;
    private String seatNo;
    private String trainClass;
    private String trainBerth;
    private String transactionID;
    private double totalAmount;

    @ManyToOne()
    @JoinColumn(name = "train_id")
    private Train train;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(String reservationDate) {
		this.reservationDate = reservationDate;
	}

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}

	public String getTrainClass() {
		return trainClass;
	}

	public void setTrainClass(String trainClass) {
		this.trainClass = trainClass;
	}

	public String getTrainBerth() {
		return trainBerth;
	}

	public void setTrainBerth(String trainBerth) {
		this.trainBerth = trainBerth;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Train getTrain() {
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Reservation(int id, String reservationDate, String seatNo, String trainClass, String trainBerth,
			String transactionID, double totalAmount, Train train, User user) {
		super();
		this.id = id;
		this.reservationDate = reservationDate;
		this.seatNo = seatNo;
		this.trainClass = trainClass;
		this.trainBerth = trainBerth;
		this.transactionID = transactionID;
		this.totalAmount = totalAmount;
		this.train = train;
		this.user = user;
	}

	public Reservation() {
		super();
		// TODO Auto-generated constructor stub
	}

}
