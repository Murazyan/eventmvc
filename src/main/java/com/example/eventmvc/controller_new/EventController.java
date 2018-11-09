//package com.example.eventmvc.controller_new;
//
//import com.example.eventmvc.model.Event;
//import com.example.eventmvc.model.EventStatus;
//import com.example.eventmvc.model.EventUsers;
//import com.example.eventmvc.model.UserEventNotification;
//import com.example.eventmvc.repository.*;
//import com.example.eventmvc.security.CurrentUser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.io.File;
//import java.io.IOException;
//
//
//@Controller
//public class EventController {
//
//    @Value("${image.folder}")
//    private String imageUploadDir;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private EventRepository eventRepository;
//
//    @Autowired
//    private EventUsersRepository eventUsersRepository;
//
//    @Autowired
//    private UserEventStatusRepository userEventStatusRepository;
//
//    @Autowired
//    private UserEventNotificationRepository userEventNotificationRepository;
//
//    @PostMapping("/add-event")
//    public String addUser(@Header("user-agent") String userAgent, @AuthenticationPrincipal CurrentUser currentUser,
//                          @ModelAttribute Event event,
//                          @RequestParam(name = "picture") MultipartFile multipartFile) throws IOException {
//
//        File file = new File(imageUploadDir);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        String filName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
//        multipartFile.transferTo(new File(imageUploadDir + filName));
//        event.setCreaterUser(currentUser.getUser());
//        event.setPicUrl(filName);
//        event.setEventStatus(EventStatus.Ընթացիկ);
//        eventRepository.save(event);
//        return "redirect:/user_page";
//    }
//
//
//    @GetMapping("/add-event-page")
//    public String pageRegister(
//            ModelMap modelMap) {
//        modelMap.addAttribute("event", new Event());
//
//        return "add-event";
//    }
//
//    @GetMapping("/seeEvent")
//    public String seeEvent(ModelMap modelMap,
//                           @AuthenticationPrincipal CurrentUser currentUser,
//                           @RequestParam(value = "eventId") int eventId) {
//
//        Event event = eventRepository.findAllById(eventId);
//        modelMap.addAttribute("current_event", event);
//        return "see-event";
//    }
//
//    @PostMapping("/cancelEvent")
//    public String cancelEvent(RedirectAttributes redirectAttributes,
//                              @RequestParam(name = "canceledEventId")int eventId,
//                              @RequestParam(name = "contactUserId") String contactUserId){
//        EventUsers eventUser = eventUsersRepository.findAllByUserAndEvent(userRepository.findAllById(Integer.parseInt(contactUserId)), eventRepository.findAllById(eventId));
//        eventUser.setUserStatus(userEventStatusRepository.findAllById(3));
//        userEventNotificationRepository.save(UserEventNotification.builder()
//                .readingStatus(false)
//                .event(eventRepository.findAllById(eventId))
//                .user(userRepository.findAllById(Integer.parseInt(contactUserId)))
//                .notificationNumber(6)
//                .build());
//        eventUsersRepository.save(eventUser);
//        redirectAttributes.addAttribute("eventId", eventId);
//        return "redirect:/seeContacts";
//    }
//
//
////    @PostMapping("/chear-event")
////    public String searchEvent(ModelMap modelMap,
////                              @RequestParam(value = "keyword") String text){
////        List<Event> result = new ArrayList<>();
////        List<Event> events = eventRepository.findAllByEventAccessType(true);
////        for (Event event : events) {
////            if(event.getEventName().contains(text)){
////                result.add(event);
////            }
////        }
////        modelMap.addAllAttributes("searchUsers")
////
////    }
//}
