package com.example.eventmvc.repository;
import com.example.eventmvc.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventUsersRepository extends JpaRepository<EventUsers, Integer> {

    List<EventUsers> findAllByUser(User user);


    List<EventUsers> findAllByEventAndUser(Event eventById, User user);
    List<EventUsers> findAllByEvent(Event eventById);
    List<EventUsers> findAllByUserOrderByIdDesc(User eventById);
    List<EventUsers> findAllByUserAndEventOrderByIdDesc(User user, Event event);
    EventUsers findAllByUserAndEvent(User user, Event event);
    List<EventUsers> findTop500ByUserOrderByIdDesc(User user);

}
