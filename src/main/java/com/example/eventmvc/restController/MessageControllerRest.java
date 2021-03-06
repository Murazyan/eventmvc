package com.example.eventmvc.restController;


import com.example.eventmvc.dto.MessageDto;
import com.example.eventmvc.dto.MessageDto1;
import com.example.eventmvc.model.Event;
import com.example.eventmvc.model.Message;
import com.example.eventmvc.model.User;
import com.example.eventmvc.repository.EventRepository;
import com.example.eventmvc.repository.MessageRepository;
import com.example.eventmvc.repository.UserRepository;
import com.example.eventmvc.security.CurrentUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MessageControllerRest {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;


    @PostMapping("/seeMessages")
    public ResponseEntity seeMessagesByToUserOrEventCreaterUser(@AuthenticationPrincipal CurrentUser currentUser,
                                                                @RequestParam(name = "eventId") int eventId,
                                                                @RequestParam(name = "participatingUserId") int participatingUserId) {
        Event currentEvent = eventRepository.findAllById(eventId);
        User participatingUser = userRepository.findAllById(participatingUserId);
        Page<Message> messagesList = messageRepository.findAllByToUserAndEventOrderByIdDesc(participatingUser, currentEvent, PageRequest.of(0, 45));
        int messageCount = messageRepository.countAllByToUserAndEventOrderByIdDesc(participatingUser, currentEvent);
        int maxCountScroll = messageCount / 45;
        List<Message> changing = messageRepository.findAllByToUserAndEventAndReadingStatusOrderByIdDesc(participatingUser, currentEvent, false);
        for (Message message : changing) {
            if (message.getToUser().getId() == currentUser.getUser().getId() && message.isCraterSendStatus())
                message.setReadingStatus(true);
            messageRepository.save(message);
            if (message.getEvent().getCreaterUser().getId() == currentUser.getUser().getId() && !message.isCraterSendStatus()) {
                message.setReadingStatus(true);
                messageRepository.save(message);
            }
        }
        List<MessageDto1> result = new ArrayList<>(45);
        if (messagesList.getSize() > 0) {
            for (Message message : messagesList) {
                result.add(MessageDto1.builder()
                        .createrSendStatus(message.isCraterSendStatus())
                        .eventCreaterUserNickname(message.getEvent().getCreaterUser().getNickname())
                        .messageDate(message.getDate())
                        .messageText(message.getText())
                        .toUserNickname(message.getToUser().getNickname())
                        .currentUserId(currentUser.getUser().getId())
                        .participatingUserId(message.getToUser().getId())
                        .eventCreaterUserId(message.getEvent().getCreaterUser().getId())
                        .build());
            }
        }
        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        class Result {
            private List<MessageDto1> messagesList;
            private int maxCountScroll;
        }
        return ResponseEntity.ok(new Result(result, maxCountScroll));
    }

    @PostMapping("/plus45Messages")
    public ResponseEntity plus45Messages(@AuthenticationPrincipal CurrentUser currentUser,
                                         @RequestParam(name = "eventId") int eventId,
                                         @RequestParam(name = "participatingUserId") int participatingUserId,
                                         @RequestParam(name = "page") int page) {
        Event currentEvent = eventRepository.findAllById(eventId);
        User participatingUser = userRepository.findAllById(participatingUserId);
        Page<Message> messagesList = messageRepository.findAllByToUserAndEventOrderByIdDesc(participatingUser, currentEvent, PageRequest.of(page, 45));
        List<MessageDto1> result = new ArrayList<>(10);
        for (Message message : messagesList) {
            result.add(MessageDto1.builder()
                    .createrSendStatus(message.isCraterSendStatus())
                    .eventCreaterUserNickname(message.getEvent().getCreaterUser().getNickname())
                    .messageDate(message.getDate())
                    .messageText(message.getText())
                    .toUserNickname(message.getToUser().getNickname())
                    .currentUserId(currentUser.getUser().getId())
                    .participatingUserId(message.getToUser().getId())
                    .eventCreaterUserId(message.getEvent().getCreaterUser().getId())
                    .build());
        }
        return ResponseEntity.ok(result);
    }

}
