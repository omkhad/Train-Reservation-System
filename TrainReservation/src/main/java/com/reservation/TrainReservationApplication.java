package com.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class TrainReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainReservationApplication.class, args);
	}

}
