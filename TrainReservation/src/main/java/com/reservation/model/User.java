package com.reservation.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String emailID;
    private String password;
    private String contact;
    private String address;
    private String profile;
    private boolean enable;
    private String role;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @ToString.Exclude
    private List<Reservation> reservation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<Reservation> getReservation() {
		return reservation;
	}

	public void setReservation(List<Reservation> reservation) {
		this.reservation = reservation;
	}

	public User(Long id, String firstName, String lastName, String emailID, String password, String contact,
			String address, String profile, boolean enable, String role, List<Reservation> reservation) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailID = emailID;
		this.password = password;
		this.contact = contact;
		this.address = address;
		this.profile = profile;
		this.enable = enable;
		this.role = role;
		this.reservation = reservation;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", emailID=" + emailID
				+ ", password=" + password + ", contact=" + contact + ", address=" + address + ", profile=" + profile
				+ ", enable=" + enable + ", role=" + role + ", reservation=" + reservation + "]";
	}
    
    

}
