package com.example.eventmvc.controller;

import com.example.eventmvc.model.Event;
import com.example.eventmvc.repository.EventRepository;
import com.example.eventmvc.repository.EventStatusCRepository;
import com.example.eventmvc.repository.UserRepository;
import com.example.eventmvc.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EventController {

    @Value("${image.folder}")
    private String imageUploadDir;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventStatusCRepository  eventStatusCRepository;


    @PostMapping("/add-event")
    public String addUser(@AuthenticationPrincipal CurrentUser currentUser,
                            @ModelAttribute Event event,
                          @RequestParam(name = "picture") MultipartFile multipartFile) throws IOException {

        File file = new File(imageUploadDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        multipartFile.transferTo(new File(imageUploadDir + filName));
        event.setCreaterUser(currentUser.getUser());
        event.setPicUrl(filName);
        eventRepository.save(event);
        return "redirect:/user_page";
    }


    @GetMapping("/add-event-page")
    public String pageRegister(
            ModelMap modelMap) {
        modelMap.addAttribute("event", new Event());
        modelMap.addAttribute("allstatus", eventStatusCRepository.findAll());
        return "add-event";
    }

    @GetMapping("/seeEvent")
    public String seeEvent(ModelMap modelMap,
                           @AuthenticationPrincipal CurrentUser currentUser,
                           @RequestParam(value = "eventId") int eventId) {
        Event event = eventRepository.findAllById(eventId);
        modelMap.addAttribute("current_event", event);
        return "see-event";
    }


//    @PostMapping("/chear-event")
//    public String searchEvent(ModelMap modelMap,
//                              @RequestParam(value = "keyword") String text){
//        List<Event> result = new ArrayList<>();
//        List<Event> events = eventRepository.findAllByEventAccessType(true);
//        for (Event event : events) {
//            if(event.getEventName().contains(text)){
//                result.add(event);
//            }
//        }
//        modelMap.addAllAttributes("searchUsers")
//
//    }
}
