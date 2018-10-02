package com.example.eventmvc.repository;



import com.example.eventmvc.model.UserEventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEventStatusRepository extends JpaRepository<UserEventStatus, Integer> {
    UserEventStatus findAllById(int id);

}
