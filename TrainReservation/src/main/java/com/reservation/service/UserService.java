package com.reservation.service;

import com.reservation.model.User;

public interface UserService {

    public User createUser(User user);

    public boolean checkEmail(String email);
}
