package com.example.eventmvc.scheduler;

//import am.arssystems.eventtrackerrest.model.User;

import com.example.eventmvc.model.Event;
import com.example.eventmvc.model.EventStatus;
import com.example.eventmvc.model.EventUsers;
import com.example.eventmvc.model.UserEventNotification;
import com.example.eventmvc.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
public class Scheduler {
//@Scheduled(cron = "0/1000 * * * * *?"/*,zone = "Armenian/Erevan"*/) Հնարավոր է նաև այս ձևով, այս դեպքում աշխատում է ամեն մեկ վարկյանը մեկ

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserEventNotificationRepository userEventNotificationRepository;

    @Autowired
    private EventUsersRepository eventUsersRepository;

    @Autowired
    private UserEventStatusRepository userEventStatusRepository;

    static final String PATTERN = "yyyy-MM-dd";
    static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(PATTERN);

    @Scheduled(fixedDelay = 10800000 /*3 ժամ*/) //1ժամ =3600000
    public void schedulerForNotificationsReminder() {
        String date = SIMPLE_DATE_FORMAT.format(new Date());
        String[] dateInfo = date.split("-");
        int year = Integer.parseInt(dateInfo[0]);
        int month = 0;
        int day = 0;
        if (dateInfo[1].substring(0, 1).equals("0")) {
            month = Integer.parseInt(dateInfo[1].substring(1));
        } else {
            month = Integer.parseInt(dateInfo[1]);
        }
        if (dateInfo[2].substring(0, 1).equals("0")) {
            day = Integer.parseInt(dateInfo[2].substring(1));
        } else {
            day = Integer.parseInt(dateInfo[2]);
        }

        List<Event> events = eventRepository.findAllByEventStatus(EventStatus.Ընթացիկ);
        for (Event event : events) {
            String occurDate = event.getOccurDate();
            String[] occurDateInfo = occurDate.split(" ");
            String[] eventDate = occurDateInfo[0].split("-");
            int eventYear = Integer.parseInt(eventDate[0]);
            int eventMonth = 0;
            if (eventDate[1].substring(0, 1).equals("0")) {
                eventMonth = Integer.parseInt(eventDate[1].substring(1));
            } else {
                eventMonth = Integer.parseInt(eventDate[1]);

            }
            int eventDay = 0;
            if (eventDate[2].substring(0, 1).equals("0")) {
                eventDay = Integer.parseInt(eventDate[2].substring(1));
            } else {
                eventDay = Integer.parseInt(eventDate[2]);
            }
            int eventHours =0;
            if(occurDateInfo[1].substring(0,1).equals("0")){
                eventHours = Integer.parseInt(occurDateInfo[1].substring(1,2));
            }else{
                eventHours = Integer.parseInt(occurDateInfo[1].substring(0,2));
            }
            int eventMinute = 0;
            if(occurDateInfo[1].split(":")[1].startsWith("0")){
                eventMinute = Integer.parseInt(occurDateInfo[1].substring(4,5));
            }else{
                eventMinute = Integer.parseInt(occurDateInfo[1].substring(3,5));
            }

            if (year == eventYear && month == eventMonth && day - 1 == eventDay) {
                List<EventUsers> eventUsers = eventUsersRepository.findAllByEventAndUserStatus(event, userEventStatusRepository.findAllById(1)); //այն eventUsers-ները որոնց ստատուսը ՛գրանցված՛ է
                for (EventUsers eventUser : eventUsers) {
                    userEventNotificationRepository.save(UserEventNotification.builder()
                            .user(eventUser.getUser())
                            .event(eventUser.getEvent())
                            .notificationNumber(5)
                            .readingStatus(false)
                            .build());
                }
            }

        }
    }
}