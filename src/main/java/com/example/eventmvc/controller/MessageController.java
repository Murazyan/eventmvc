package com.example.eventmvc.controller;

import com.example.eventmvc.model.Message;
import com.example.eventmvc.repository.MessageRepository;
import com.example.eventmvc.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
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
        List<Message> messagesList = messageRepository.findAllByToUserOrEvent_CreaterUserOrderByIdDesc(currentUser.getUser(), currentUser.getUser());
        Set<Message> messageSet = new LinkedHashSet<>(messagesList);
        modelMap.addAttribute("messageList", messageSet);
        modelMap.addAttribute("currentUser", currentUser.getUser());
        return "face_messages";
    }
}
