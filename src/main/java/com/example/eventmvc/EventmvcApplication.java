package com.example.eventmvc;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



import java.net.MalformedURLException;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

import static java.rmi.server.LogStream.log;


@SpringBootApplication
public class EventmvcApplication   {


    public static void main(String[] args) throws MalformedURLException {
//        սա սերվերի ժամը դարձնում է UTC ժամ
//        org.joda.time.DateTime now = new org.joda.time.DateTime(); // Default time zone.
//        org.joda.time.DateTime zulu = now.toDateTime( org.joda.time.DateTimeZone.UTC );
//        System.out.println( "Local time in ISO 8601 format: " + now );
//        System.out.println( "Same moment in UTC (Zulu): " + zulu );

//        String time = new SimpleDateFormat("HH:mm").format(new Date());
//        System.out.println("time is " +time);
//        SpringApplication.run(EventmvcApplication.class, args);
//        Calendar calendar = Calendar.getInstance();
//        int i = calendar.get(Calendar.MINUTE);
//        int vi = calendar.get(Calendar.HOUR);
//        System.out.println("Ժամը --"+vi);
//        System.out.println("Րոպե --"+i);
    }
}
