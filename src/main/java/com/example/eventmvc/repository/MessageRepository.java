package com.example.eventmvc.repository;


import com.example.eventmvc.model.Event;
import com.example.eventmvc.model.Message;
import com.example.eventmvc.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findAllByToUserOrEvent_CreaterUserOrderByIdDesc(User user,User eventCreaterUser);
    int countAllByCraterSendStatusAndToUserAndEventAndReadingStatus(boolean status, User user, Event event, boolean readingStatus);
    int countAllByToUserAndEventOrderByIdDesc(User user, Event event);
    Page<Message> findAllByToUserAndEventOrderByIdDesc(User user, Event event,Pageable pageable);
    List<Message> findAllByToUserAndEventOrderById(User user, Event event);
    Page<Message> findAllByToUserAndEventOrderById(User user, Event event,Pageable pageable);
    List<Message> findAllByToUserAndEventAndReadingStatusOrderByIdDesc(User user, Event event, boolean status);
    Page<Message> findAllByToUser(User user, Pageable pageable);



}
