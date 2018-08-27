package com.example.eventmvc.repository;

import com.example.eventmvc.model.Event;
import com.example.eventmvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Integer> {
    List<Event> findAllByCreaterUser(User user);
    List<Event> findAllByEventAccessType(boolean t);
    Event findAllById(Integer id);
}
