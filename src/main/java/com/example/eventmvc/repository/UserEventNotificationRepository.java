package com.example.eventmvc.repository;



import com.example.eventmvc.model.Event;
import com.example.eventmvc.model.EventUsers;
import com.example.eventmvc.model.UserEventNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEventNotificationRepository extends JpaRepository<UserEventNotification, Integer> {
  UserEventNotification findTop1ByEvent(Event event);


}
