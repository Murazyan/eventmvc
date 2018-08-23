package com.example.eventmvc.repository;

import com.example.eventmvc.model.EventUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventUsersRepository extends JpaRepository<EventUsers, Integer> {


}
