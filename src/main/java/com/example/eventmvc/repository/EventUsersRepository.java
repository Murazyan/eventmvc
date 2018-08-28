package com.example.eventmvc.repository;

import com.example.eventmvc.model.EventUsers;
import com.example.eventmvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventUsersRepository extends JpaRepository<EventUsers, Integer> {

List<EventUsers> findAllByUserOrderByIdDesc(User user);
}
