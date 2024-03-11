package com.reservation.repository;

import com.reservation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.emailID = :emailID")
    public User getUserByUsername(@Param("emailID") String emailID);

    public Optional<User> findByEmailID(String email);

    public boolean existsByEmailID(String email);

}
