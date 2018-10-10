package com.example.eventmvc.repository;


import com.example.eventmvc.model.Event;
import com.example.eventmvc.model.Message;
import com.example.eventmvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findAllByToUserAndReadingStatus(User user, boolean readingStatus);
    List<Message> findAllByToUserAndEvent(User toUser, Event event);
    List<Message> findAllByEvent(Event event);
    List<Message> findAllByToUser(User user);
    List<Message> findAllByToUserOrEvent(User user, Event event);
    List<Message> findAllByToUserOrEventOrderByIdDesc(User user, Event event);
    List<Message> findAllByToUserOrEvent_CreaterUserOrderByIdDesc(User user,User eventCreaterUser);


}
