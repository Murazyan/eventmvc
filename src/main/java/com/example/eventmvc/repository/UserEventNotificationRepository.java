package com.example.eventmvc.repository;



import com.example.eventmvc.model.Event;
import com.example.eventmvc.model.EventUsers;
import com.example.eventmvc.model.User;
import com.example.eventmvc.model.UserEventNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserEventNotificationRepository extends JpaRepository<UserEventNotification, Integer> {
  UserEventNotification findTop1ByEvent(Event event);
  List<UserEventNotification> findTop400ByUserOrderByIdDesc(User user);
  int countAllByUserAndReadingStatus(User user, boolean b);
  UserEventNotification findTopByEvent(Event event);
  List<UserEventNotification> findAllByEventAndUserAndNotificationNumber(Event event, User user,int notificationNumber);


}
