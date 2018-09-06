package com.example.eventmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class GoogleMapController {
    @GetMapping("/map")
    public String map(){
        return "google-map";
    }
    @GetMapping("/map1")
    public String map1(){
        return "add-eventor-original";
    }
}
