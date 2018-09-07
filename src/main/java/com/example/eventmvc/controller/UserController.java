package com.example.eventmvc.controller;

import com.example.eventmvc.mail.EmailServiceImpl;
import com.example.eventmvc.model.*;
import com.example.eventmvc.repository.EventRepository;
import com.example.eventmvc.repository.EventUsersRepository;
import com.example.eventmvc.repository.UserEventStatusRepository;
import com.example.eventmvc.repository.UserRepository;
import com.example.eventmvc.security.CurrentUser;
import com.example.eventmvc.security.JwtTokenUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class UserController {


    @Value("${image.folder}")
    private String imageUploadDir;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventUsersRepository eventUsersRepository;

    @Autowired
    private UserEventStatusRepository userEventStatusRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    String pattern = "YYYY-DD-MM";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);


    @GetMapping("/home")
    public String index(ModelMap modelMap,
                        @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            return "redirect:/user_page";
        }
        modelMap.addAttribute("isLoggedIn", userDetails != null);
        return "index-01";
    }



    @GetMapping("/page-login")
    public String pageLogin(ModelMap map) {
        map.addAttribute("login_user", new User());
        return "page-login";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(ModelMap modelMap,
                               @AuthenticationPrincipal UserDetails userDetails) {
        CurrentUser currentUser = (CurrentUser) userDetails;
        if (currentUser != null) {
            User user = currentUser.getUser();
            if(user.isFirst_access_after_forgot()){
                modelMap.addAttribute("currentUserUsername", user.getUsername());
                return "redirect:/change-password";
            }
            return "redirect:/user_page";
        } else return "redirect:/index";
    }
    @GetMapping("/change-password")
    public String changePassword(){
        return "change-password";
    }

    @PostMapping("/addContact")
    public String addContact(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(value = "userId") Integer userId) {
        User contactUser = userRepository.findAllById(userId);
        // List<User> result = new ArrayList<>();
        //  result.add(contactUser);
        CurrentUser currentUser = (CurrentUser) userDetails;
        User user = currentUser.getUser();
        List<User> contactUser1 = user.getContactUser();
        contactUser1.add(contactUser);
        user.setContactUser(contactUser1);
        userRepository.save(user);
        return "redirect:/user_page";
    }

    @GetMapping("/user_page")
    public String bloglgpostgrid(ModelMap modelMap,
                                 @AuthenticationPrincipal CurrentUser currentUser) {

        modelMap.addAttribute("currentUser", currentUser.getUser());
        modelMap.addAttribute("currentUsersEvent", eventRepository.findAllByCreaterUser(currentUser.getUser()));

        List<EventUsers> allByUserOrderByIdDesc = eventUsersRepository.findAllByUserOrderByIdDesc(userRepository.findAllById(currentUser.getUser().getId()));
        HashSet<EventUsers> hashSet = new HashSet<>(allByUserOrderByIdDesc);
//        hashSet.addAll(allByUserOrderByIdDesc);
        List<Event> eventsCurrentUser = new ArrayList<>(hashSet.size());
        for (EventUsers eventUsers : hashSet) {
            eventsCurrentUser.add(eventUsers.getEvent());
        }

        modelMap.addAttribute("eventsOfCurrentUser", eventsCurrentUser);
        modelMap.addAttribute("isLoggedIn", currentUser != null);

        return "user-page";
    }

    @PostMapping("/register")
    public String addUser(@ModelAttribute User user, @RequestParam(name = "picture") MultipartFile multipartFile) throws IOException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        File file = new File(imageUploadDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        multipartFile.transferTo(new File(imageUploadDir + filName));
        user.setPicUrl(filName);
        user.setToken(jwtTokenUtil.generateToken(user.getUsername()));
        userRepository.save(user);
        String url = String.format("http://localhost:8081/verify?token=%s&username=%s", user.getToken(), user.getUsername());
        String text = String.format("Dear %s Thank you, you have successfully registered to  our Evens_Tracker. Please visit by link in order to activate your profile.  %s", user.getNickname(), url);
        emailService.sendSipmleMessage(user.getUsername(), "Իրադարձությունների վերահսկում", text);
        return "redirect:/page-login";
    }

    @GetMapping(value = "/verify")
    public String verify(ModelMap modelMap,
                         @RequestParam("token") String token,
                         @RequestParam("username") String username) {

        User allByUsername = userRepository.findAllByUsername(username);
        try {
            if (allByUsername.getToken() == null) {
                modelMap.addAttribute("isfirst", "1");
            }
            if (allByUsername != null) {
                if (allByUsername.getToken() != null && allByUsername.getToken().equals(token)) {
                    allByUsername.setToken(null);
                    //  allByUsername.setVerify(true);
                    userRepository.save(allByUsername);
                    modelMap.addAttribute("isfirst", "0");
                }

            }
        } catch (Exception e) {
            modelMap.addAttribute("isfirst", "2");

        }
        return "mail-verify";
    }

    @GetMapping("/page-register")
    public String pageRegister(ModelMap modelMap) {
        modelMap.addAttribute("user", new User());
        return "page-register";
    }

    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public void getImageAsByteArray(HttpServletResponse response,
                                    @RequestParam("fileName") String fileName) throws IOException {
        InputStream in = new FileInputStream(imageUploadDir + fileName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

    @GetMapping("/searchUser")
    public String searchUser(ModelMap map, @RequestParam(value = "keyword") String text) {
        List<User> result = new ArrayList<>();
        List<User> all = userRepository.findAll();
        for (User user : all) {
            if (user.getNickname().contains(text)) {
                result.add(user);
            }
        }
        map.addAttribute("searchUser", result);
        return "searchUser";
    }

    @PostMapping("/searchEvent")
    public String searchUSer(ModelMap modelMap, @RequestParam(value = "eventName") String eventName) {
        List<Event> result = new ArrayList<>();
        List<Event> all = eventRepository.findAll();
        for (Event event : all) {
            if (event.isEventAccessType() && event.getEventName().contains(eventName) && event.getEventStatus().getStatus().equals("Ընթացիկ")) {
                result.add(event);
            }
        }
        modelMap.addAttribute("searchEvent", result);
        return "searchEvent";
    }

    @PostMapping("/addmyevent")
    public String addMyEvent(@RequestParam(value = "eventId") Integer eventId,
                             @AuthenticationPrincipal CurrentUser currentUser) {
        User user = currentUser.getUser();
        Event event = eventRepository.findAllById(eventId);
        EventUsers eventUsers = new EventUsers();
        eventUsers.setEvent(event);
        eventUsers.setUser(user);
        UserEventStatus byId = userEventStatusRepository.findAllById(1);
        eventUsers.setUserStatus(byId);
        eventUsersRepository.save(eventUsers);
        return "redirect:/user_page";
    }

    @GetMapping("/verifyError")
    public String verifyError() {
        return "verifyError";
    }
//    @GetMapping("/map")
//    public String gMap() {
//        return "shortcode-gmap";
//    }

    @GetMapping("/seeContacts")
    public String seeContacts(ModelMap modelMap,
                              @AuthenticationPrincipal CurrentUser currentUser,
                              @RequestParam(value = "eventId") String eventId) {
        User user = currentUser.getUser();
        modelMap.addAttribute("contactUser", user.getContactUser());
        modelMap.addAttribute("eventId", eventId);
        return "addperson";
    }

    @PostMapping("validMail")
    public String validMail(ModelMap modelMap,
                                  @RequestParam(value = "email") String email) {
        User user = userRepository.findAllByUsername(email);
        modelMap.addAttribute("userFound", user!=null);
        if(user!=null){
            String newPassword = getRandomPassword(6);
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setFirst_access_after_forgot(true);
            userRepository.save(user);
            String text = "Ձեզ համար գեներացվել է մեկանգամյա օգտագործման  գաղտնաբառ`  " + newPassword;
            emailService.sendSipmleMessage(user.getUsername(), "Իրադարձությունների վերահսկում - գաղտնաբառի վերականգնում", text);
            return "redirect:/page-login";
        }else{
            modelMap.addAttribute("userNotFound", "Սխալ մուտքագրված էլ-հասցե");
        }
        return "forgot-password";
    }

    public static String getRandomPassword(int length_forgotPassword) {

        final Random random = new Random();
        final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890!@#$*%&";
        StringBuilder password = new StringBuilder(length_forgotPassword);
        for (int i = 0; i < length_forgotPassword; i++) {
            password.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return password.toString();
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {

        return "forgot-password";
    }

    @PostMapping("/changeMyPassword")
    public String changeMyPassword(@AuthenticationPrincipal CurrentUser currentUser,
                                   @RequestParam(value = "newPass")String newPass){
        User user = currentUser.getUser();
        if(user==null){
            return "redirect:/page-login";
        }
        user.setFirst_access_after_forgot(false);
        user.setPassword(passwordEncoder.encode(newPass));
        userRepository.save(user);
        return "redirect:/user_page";
    }


}
