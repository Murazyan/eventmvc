package com.example.eventmvc.controller;

import com.example.eventmvc.model.User;
import com.example.eventmvc.repository.EventRepository;
import com.example.eventmvc.repository.UserRepository;
import com.example.eventmvc.security.CurrentUser;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class UserController {


    @Value("${image.folder}")
    private String imageUploadDir;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


    String pattern = "YYYY-DD-MM";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);


    @GetMapping("/index-01")
    public String index() {
        return "index-01";
    }


    @GetMapping("/index")
    public String index1() {
        return "index";
    }

    @GetMapping("/page-login")
    public String pageLogin(ModelMap map) {
        map.addAttribute("login_user", new User());
        return "page-login";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(@AuthenticationPrincipal UserDetails userDetails) {
        CurrentUser currentUser = (CurrentUser) userDetails;
        if (currentUser != null) {
            return "redirect:/user_page";
        } else return "redirect:/index";
    }

    @GetMapping("/user_page")
    public String bloglgpostgrid(ModelMap modelMap,
                                 @AuthenticationPrincipal CurrentUser currentUser) {
        modelMap.addAttribute("currentUser", currentUser.getUser());
        modelMap.addAttribute("currentUsersEvent", eventRepository.findAllByCreaterUser(currentUser.getUser()));

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
        userRepository.save(user);
        return "redirect:/page-login";
    }
    @GetMapping("/page-register")
    public String pageRegister(ModelMap modelMap) {
        modelMap.addAttribute("user", new User());
        return "page-register";
    }

    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public void getImageAsByteArray(HttpServletResponse response,
                                    @RequestParam("fileName" ) String fileName) throws IOException {
        InputStream in = new FileInputStream(imageUploadDir + fileName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }
}
