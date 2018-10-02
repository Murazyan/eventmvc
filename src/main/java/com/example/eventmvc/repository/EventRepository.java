package com.example.eventmvc.repository;


import com.example.eventmvc.model.Event;
import com.example.eventmvc.model.PreferencesThemes;
import com.example.eventmvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
        List<Event> findTopByPreferencesThemes(PreferencesThemes preferencesThemes);
        Event findAllByPicUrl(String picUrl);
        List<Event> findAllByEventNameAndEventAccessType(String eventName, boolean accessType);
        List<Event> findAllByCreaterUser(User user);
        List<Event> findAllByEventAccessTypeOrderByIdDesc(boolean b);
        Event findAllById(int id);





}
