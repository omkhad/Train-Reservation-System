package com.reservation.service.impl;

import com.reservation.model.User;
import com.reservation.repository.UserRepository;
import com.reservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean checkEmail(String email) {
        return userRepository.existsByEmailID(email);
    }
}
