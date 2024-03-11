package com.reservation.model;

import lombok.*;


public class Otp {
    private String Otp;

	public String getOtp() {
		return Otp;
	}

	public void setOtp(String otp) {
		Otp = otp;
	}

	public Otp(String otp) {
		super();
		Otp = otp;
	}

	public Otp() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}
