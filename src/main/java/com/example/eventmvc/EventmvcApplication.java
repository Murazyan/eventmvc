package com.example.eventmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLOutput;
import java.util.Calendar;


@SpringBootApplication
public class EventmvcApplication {



    public static void main(String[] args) {
        SpringApplication.run(EventmvcApplication.class, args);
        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(Calendar.MINUTE);
        int vi = calendar.get(Calendar.HOUR);
        System.out.println("Ժամը --"+vi);
        System.out.println("Րոպե --"+i);
    }
}
