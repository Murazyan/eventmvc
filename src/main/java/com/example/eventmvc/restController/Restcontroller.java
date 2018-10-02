package com.example.eventmvc.restController;

import com.example.eventmvc.model.Event;
import com.example.eventmvc.model.EventUsers;
import com.example.eventmvc.model.User;
import com.example.eventmvc.model.UserEventNotification;
import com.example.eventmvc.repository.EventRepository;
import com.example.eventmvc.repository.EventUsersRepository;
import com.example.eventmvc.repository.UserEventNotificationRepository;
import com.example.eventmvc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.eventmvc.dto.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Restcontroller {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventUsersRepository eventUsersRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserEventNotificationRepository userEventNotificationRepository;

    @GetMapping("search-contact-by-nickwithJson")
    public ResponseEntity searchContactByUsernameAjaxWithJson(@RequestParam(name = "key-nickname") String keyUsername,
                                                              @RequestParam(name = "eventId")int eventId) {
       Event currentEvent = eventRepository.findAllById(eventId);
        if(keyUsername.equals("")){
            return ResponseEntity.ok(Result.builder()
                    .username(null)
                    .picUrl(null)
                    .nickname(null)
                    .build());
        }
        List<User> result = userRepository.findAllByNicknameContaining(keyUsername);
        List<Result> json = new ArrayList<>(result.size());
        for (User user : result) {
            EventUsers userAndEvent = eventUsersRepository.findAllByUserAndEvent(user, currentEvent);
            if(userAndEvent==null){
                System.out.println("useri idin e -" +user.getId());
                json.add(Result.builder()
                        .userId(user.getId())
                        .username(user.getUsername())
                        .nickname(user.getNickname())
                        .picUrl(user.getPicUrl())
                        .isNull(0)
                        .build());
            }else{
                System.out.println("useri idin e  --" +user.getId());
                json.add(Result.builder()
                        .userId(user.getId())
                        .username(user.getUsername())
                        .nickname(user.getNickname())
                        .picUrl(user.getPicUrl())
                        .isNull(userAndEvent.getUserStatus().getId())
                        .build());
            }
        }
        return ResponseEntity.ok(json);
    }

}
