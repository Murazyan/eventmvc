package com.example.eventmvc.repository;

import com.example.eventmvc.model.EventStatusC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventStatusCRepository extends JpaRepository<EventStatusC,Integer> {


}
