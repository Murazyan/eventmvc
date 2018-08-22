package com.example.eventmvc.repository;

import com.example.eventmvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findAllByUsername(String s);
}
