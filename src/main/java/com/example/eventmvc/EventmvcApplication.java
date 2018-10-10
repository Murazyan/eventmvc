package com.example.eventmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@SpringBootApplication
public class EventmvcApplication {



    public static void main(String[] args) throws MalformedURLException {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        System.out.println("time is " +time);
        SpringApplication.run(EventmvcApplication.class, args);
        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(Calendar.MINUTE);
        int vi = calendar.get(Calendar.HOUR);
        System.out.println("Ժամը --"+vi);
        System.out.println("Րոպե --"+i);
    }
}
