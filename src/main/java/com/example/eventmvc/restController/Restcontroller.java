package com.example.eventmvc.restController;

import com.example.eventmvc.model.Event;
import com.example.eventmvc.model.EventUsers;
import com.example.eventmvc.model.User;
import com.example.eventmvc.model.UserEventNotification;
import com.example.eventmvc.repository.*;
import com.example.eventmvc.security.CurrentUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Autowired
    private UserEventStatusRepository userEventStatusRepository;



    @PostMapping("/seeAllNotification")
    public ResponseEntity seeAllNotification(@AuthenticationPrincipal CurrentUser currentUser) {
        User current_User = currentUser.getUser();
        List<UserEventNotification> notifiactions = userEventNotificationRepository.findTop400ByUserOrderByIdDesc(current_User);

        List<NotificationDto> result = new ArrayList<>(notifiactions.size());
        for (UserEventNotification notifiaction : notifiactions) {
            notifiaction.setReadingStatus(true);
            userEventNotificationRepository.save(notifiaction);
            result.add(NotificationDto.builder()
                    .eventName(notifiaction.getEvent().getEventName())
                    .eventPicUrl(notifiaction.getEvent().getPicUrl())
                    .notificationDate(notifiaction.getNotificationDate())
                    .notificationNumber(notifiaction.getNotificationNumber())
                    .build());
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/notReadingNotificationCount")
    public ResponseEntity notReadingNotificationCount(@AuthenticationPrincipal CurrentUser currentUser) {
        User user;
        try {
            user = currentUser.getUser();
        }catch (NullPointerException e){
            return ResponseEntity.ok(NotReadingNotificationCount.builder()
                    .notReadingNotificationCount(-1)
                    .build());
        }
        return ResponseEntity.ok(NotReadingNotificationCount.builder()
                .notReadingNotificationCount(userEventNotificationRepository.countAllByUserAndReadingStatus(user, false))
                .build());
    }

    @PostMapping("search-contact-by-username")
    public ResponseEntity searchContactByUsernameAjax(@RequestParam(name = "key-username") String userUsername,
                                                      @RequestParam(name = "eventId") int eventId) {
        Event currentEvent = eventRepository.findAllById(eventId);
        if (userUsername.equals("")) {
            return ResponseEntity.ok(Result.builder()
                    .username(null)
                    .picUrl(null)
                    .nickname(null)
                    .build());
        }
        List<User> result = userRepository.findAllByUsernameContaining(userUsername);
        List<Result> json = new ArrayList<>(result.size());
        for (User user : result) {
            EventUsers userAndEvent = eventUsersRepository.findAllByUserAndEvent(user, currentEvent);
            if (userAndEvent == null) {
                json.add(Result.builder()
                        .userId(user.getId())
                        .username(user.getUsername())
                        .nickname(user.getNickname())
                        .picUrl(user.getPicUrl())
                        .isNull(0)
                        .build());
            } else {
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

    @PostMapping("/cancelEventWithAyax")
    public ResponseEntity cancelEvent(@RequestParam(name = "userId") int userId,
                                      @RequestParam(name = "eventId") int eventId,
                                      @RequestParam(name = "inputValue") String inputValue) {
        User user = userRepository.findAllById(userId);
        Event currentEvent = eventRepository.findAllById(eventId);
        EventUsers eventAndUser = eventUsersRepository.findAllByUserAndEvent(user, currentEvent);
        eventAndUser.setUserStatus(userEventStatusRepository.findAllById(3));
        eventUsersRepository.save(eventAndUser);
        userEventNotificationRepository.save(UserEventNotification.builder()
                .notificationNumber(6)
                .event(currentEvent)
                .user(user)
                .readingStatus(false)
                .build());
        List<User> results = userRepository.findAllByNicknameContaining(inputValue);
        List<Result> json = new ArrayList<>(results.size());
        for (User result : results) {
            EventUsers userAndEvent = eventUsersRepository.findAllByUserAndEvent(result, currentEvent);
            if (userAndEvent == null) {
                json.add(Result.builder()
                        .userId(result.getId())
                        .username(result.getUsername())
                        .nickname(result.getNickname())
                        .picUrl(result.getPicUrl())
                        .isNull(0)
                        .build());
            } else {
                json.add(Result.builder()
                        .userId(result.getId())
                        .username(result.getUsername())
                        .nickname(result.getNickname())
                        .picUrl(result.getPicUrl())
                        .isNull(userAndEvent.getUserStatus().getId())
                        .build());
            }
        }
        return ResponseEntity.ok(json);
    }


    @PostMapping("/cancelEventWithAyaxByUSername")
    public ResponseEntity cancelEventByUSername(@RequestParam(name = "userId") int userId,
                                                @RequestParam(name = "eventId") int eventId,
                                                @RequestParam(name = "inputValue") String inputValue) {
        User user = userRepository.findAllById(userId);
        Event currentEvent = eventRepository.findAllById(eventId);
        EventUsers eventAndUser = eventUsersRepository.findAllByUserAndEvent(user, currentEvent);
        eventAndUser.setUserStatus(userEventStatusRepository.findAllById(3));
        eventUsersRepository.save(eventAndUser);
        userEventNotificationRepository.save(UserEventNotification.builder()
                .notificationNumber(6)
                .user(user)
                .event(currentEvent)
                .readingStatus(false)
                .build());
        List<User> results = userRepository.findAllByUsernameContaining(inputValue);
        List<Result> json = new ArrayList<>(results.size());
        for (User result : results) {
            EventUsers userAndEvent = eventUsersRepository.findAllByUserAndEvent(result, currentEvent);
            if (userAndEvent == null) {
                json.add(Result.builder()
                        .userId(result.getId())
                        .username(result.getUsername())
                        .nickname(result.getNickname())
                        .picUrl(result.getPicUrl())
                        .isNull(0)
                        .build());
            } else {
                json.add(Result.builder()
                        .userId(result.getId())
                        .username(result.getUsername())
                        .nickname(result.getNickname())
                        .picUrl(result.getPicUrl())
                        .isNull(userAndEvent.getUserStatus().getId())
                        .build());
            }
        }
        return ResponseEntity.ok(json);
    }


    @GetMapping("search-contact-by-nickwithJson")
    public ResponseEntity searchContactByNicknameAjax(@RequestParam(name = "key-nickname") String keyNickname,
                                                      @RequestParam(name = "eventId") int eventId) {
        Event currentEvent = eventRepository.findAllById(eventId);
        if (keyNickname.equals("")) {
            return ResponseEntity.ok(Result.builder()
                    .username(null)
                    .picUrl(null)
                    .nickname(null)
                    .build());
        }
        List<User> result = userRepository.findAllByNicknameContaining(keyNickname);
        List<Result> json = new ArrayList<>(result.size());
        for (User user : result) {
            EventUsers userAndEvent = eventUsersRepository.findAllByUserAndEvent(user, currentEvent);
            if (userAndEvent == null) {
                json.add(Result.builder()
                        .userId(user.getId())
                        .username(user.getUsername())
                        .nickname(user.getNickname())
                        .picUrl(user.getPicUrl())
                        .isNull(0)
                        .build());
            } else {
                System.out.println("useri idin e  --" + user.getId());
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

    @PostMapping("/inviteWithAyaxByUsername")
    public ResponseEntity inviteWithAyaxByUsername(@RequestParam(name = "userId") int userId,
                                                   @RequestParam(name = "eventId") int eventId,
                                                   @RequestParam(name = "inputValue") String inputValue) {
        User user = userRepository.findAllById(userId);
        Event currentEvent = eventRepository.findAllById(eventId);
        EventUsers eventAndUser = eventUsersRepository.findAllByUserAndEvent(user, currentEvent);
        if (eventAndUser == null) {
            EventUsers eventUser = new EventUsers();
            eventUser.setUser(user);
            eventUser.setEvent(currentEvent);
            eventUser.setUserStatus(userEventStatusRepository.findAllById(2));
            eventUsersRepository.save(eventUser);
            userEventNotificationRepository.save(UserEventNotification.builder()
                    .notificationNumber(4)
                    .user(user)
                    .event(currentEvent)
                    .readingStatus(false)
                    .build());
        } else {
            eventAndUser.setUserStatus(userEventStatusRepository.findAllById(2));
            eventUsersRepository.save(eventAndUser);
            userEventNotificationRepository.save(UserEventNotification.builder()
                    .notificationNumber(4)
                    .user(user)
                    .event(currentEvent)
                    .readingStatus(false)
                    .build());
        }
        List<User> results = userRepository.findAllByUsernameContaining(inputValue);
        List<Result> json = new ArrayList<>(results.size());
        for (User result : results) {
            EventUsers userAndEvent = eventUsersRepository.findAllByUserAndEvent(result, currentEvent);
            if (userAndEvent == null) {
                json.add(Result.builder()
                        .userId(result.getId())
                        .username(result.getUsername())
                        .nickname(result.getNickname())
                        .picUrl(result.getPicUrl())
                        .isNull(0)
                        .build());
            } else {
                json.add(Result.builder()
                        .userId(result.getId())
                        .username(result.getUsername())
                        .nickname(result.getNickname())
                        .picUrl(result.getPicUrl())
                        .isNull(userAndEvent.getUserStatus().getId())
                        .build());
            }
        }
        return ResponseEntity.ok(json);
    }


    @PostMapping("/inviteWithAyax")
    public ResponseEntity inviteWithAyax(@RequestParam(name = "userId") int userId,
                                         @RequestParam(name = "eventId") int eventId,
                                         @RequestParam(name = "inputValue") String inputValue) {
        User user = userRepository.findAllById(userId);
        Event currentEvent = eventRepository.findAllById(eventId);
        EventUsers eventAndUser = eventUsersRepository.findAllByUserAndEvent(user, currentEvent);
        if (eventAndUser == null) {
            EventUsers eventUser = new EventUsers();
            eventUser.setUser(user);
            eventUser.setEvent(currentEvent);
            eventUser.setUserStatus(userEventStatusRepository.findAllById(2));
            eventUsersRepository.save(eventUser);
            userEventNotificationRepository.save(UserEventNotification.builder()
                    .notificationNumber(4)
                    .user(user)
                    .event(currentEvent)
                    .readingStatus(false)
                    .build());
        } else {
            eventAndUser.setUserStatus(userEventStatusRepository.findAllById(2));
            eventUsersRepository.save(eventAndUser);
            userEventNotificationRepository.save(UserEventNotification.builder()
                    .notificationNumber(4)
                    .user(user)
                    .event(currentEvent)
                    .readingStatus(false)
                    .build());
        }
        List<User> results = userRepository.findAllByNicknameContaining(inputValue);
        List<Result> json = new ArrayList<>(results.size());
        for (User result : results) {
            EventUsers userAndEvent = eventUsersRepository.findAllByUserAndEvent(result, currentEvent);
            if (userAndEvent == null) {
                json.add(Result.builder()
                        .userId(result.getId())
                        .username(result.getUsername())
                        .nickname(result.getNickname())
                        .picUrl(result.getPicUrl())
                        .isNull(0)
                        .build());
            } else {
                json.add(Result.builder()
                        .userId(result.getId())
                        .username(result.getUsername())
                        .nickname(result.getNickname())
                        .picUrl(result.getPicUrl())
                        .isNull(userAndEvent.getUserStatus().getId())
                        .build());
            }
        }
        return ResponseEntity.ok(json);
    }
}
