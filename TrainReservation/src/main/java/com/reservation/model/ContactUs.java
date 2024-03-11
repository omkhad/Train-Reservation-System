package com.reservation.model;

public class ContactUs {
    private String fullName;
    private String email;
    private String contactNo;
    private String comments;

    public ContactUs() {
        super();
    }

    public ContactUs(String fullName, String email, String contactNo, String comments) {
        this.fullName = fullName;
        this.email = email;
        this.contactNo = contactNo;
        this.comments = comments;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
