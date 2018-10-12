package com.example.eventmvc.controller;

import com.example.eventmvc.dto.MessageDto;
import com.example.eventmvc.model.Message;
import com.example.eventmvc.model.User;
import com.example.eventmvc.repository.MessageRepository;
import com.example.eventmvc.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/faceMessage")
    public String faceMessage(@AuthenticationPrincipal CurrentUser currentUser,
                              ModelMap modelMap){
        Page<Message> safasf = messageRepository.findAllByToUser(currentUser.getUser(),   PageRequest.of(2,2));

        for (Message message : safasf) {
            System.out.println("Message text  is --- "+message.getText());
        }
        List<Message> messagesList = messageRepository.findAllByToUserOrEvent_CreaterUserOrderByIdDesc(currentUser.getUser(), currentUser.getUser());
        Set<Message> messageSet = new LinkedHashSet<>(messagesList);
        List<MessageDto> result = new ArrayList<>(messageSet.size());
        for (Message message : messageSet) {
            if(message.getToUser().getId()==currentUser.getUser().getId()){ // մտած օգտատերը տվյալ նամակի իրադարձության ստեղծողը չէ
                result.add(MessageDto.builder()
                        .message(message)
                        .notReadingCount(messageRepository.countAllByCraterSendStatusAndToUserAndEventAndReadingStatus(true,currentUser.getUser(),message.getEvent(),false))
                        .build());
            }
            if(currentUser.getUser().getId()==message.getEvent().getCreaterUser().getId()){ // մտած օգտատերը տվյալ նամակի իրադարձության ստեղծողն է
                result.add(MessageDto.builder()
                        .message(message)
                        .notReadingCount(messageRepository.countAllByCraterSendStatusAndToUserAndEventAndReadingStatus(false,message.getToUser(),message.getEvent(),false))
                        .build());
            }
        }
        modelMap.addAttribute("messageList", result);
        modelMap.addAttribute("currentUser", currentUser.getUser());
        return "face_messages";
    }
}
