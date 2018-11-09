package com.example.eventmvc.scheduler;

//import am.arssystems.eventtrackerrest.model.User;

import com.example.eventmvc.model.Event;
import com.example.eventmvc.model.EventStatus;
import com.example.eventmvc.model.EventUsers;
import com.example.eventmvc.model.UserEventNotification;
import com.example.eventmvc.repository.*;

import com.example.eventmvc.socket.socketEndpoints.SaveAndSendMessages;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@Component
public class Scheduler {

private static final int REMINDER_NOTOFICATION_NUMBER = 5;  // Հիշեցում. Այսօր ժամը __:__-ին տեղի է ունենալու ՛՛Իրադարձության անվանում՛՛իրադարձությանը:
    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler(Executors.newSingleThreadScheduledExecutor());
    }

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

//    private static final ZoneId ARMENIA_ZONE_ID = ZoneId.of("Asia/Yerevan");
//    private static LocalTime armenianTime = LocalTime.now(ARMENIA_ZONE_ID);
    private static final DateTimeZone ARMENIA_DATE_TIME_ZONE = DateTimeZone.forID("Asia/Yerevan");
    private static final DateTime ARMENIA_DATE_TIME = new DateTime(ARMENIA_DATE_TIME_ZONE);

    @Scheduled(fixedDelay = 10800000 /*3 ժամ*/) //1ժամ =3600000 3ժամ=10800000
    public void schedulerForNotificationsReminder() {
        int month = ARMENIA_DATE_TIME.getMonthOfYear();
        int day = ARMENIA_DATE_TIME.getDayOfMonth();
        int year = ARMENIA_DATE_TIME.getYear();
        int  hour = ARMENIA_DATE_TIME.getHourOfDay();
        int minute = ARMENIA_DATE_TIME.getMinuteOfHour();
        List<Event> events = eventRepository.findAllByEventStatus(EventStatus.Ընթացիկ);
        for (Event event : events) {
            String occurDate = event.getOccurDate();
//            String[] occurDateInfo = occurDate.split(" ");
            String[] eventDate = occurDate.split("-");
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
            int eventHour =0;
            if(event.getOccurDatehour().substring(0,1).equals("0")){
                eventHour = Integer.parseInt(event.getOccurDatehour().substring(1,2));
            }else{
                eventHour = Integer.parseInt(event.getOccurDatehour().substring(0,2));
            }
            int eventMinute = 0;
            if(event.getOccurDatehour().split(":")[1].startsWith("0")){
                eventMinute = Integer.parseInt(event.getOccurDatehour().substring(4,5));
            }else{
                eventMinute = Integer.parseInt(event.getOccurDatehour().substring(3,5));
            }
            if(year>=eventYear && month>=eventMonth && day>=eventDay && hour>=eventHour && minute>=eventMinute){
                event.setEventStatus(EventStatus.Ավարտված);
                eventRepository.save(event);
            }

            if (year == eventYear && month == eventMonth && day  == eventDay-1) {
                List<EventUsers> eventUsers = eventUsersRepository.findAllByEventAndUserStatus(event, userEventStatusRepository.findAllById(1)); //այն eventUsers-ները որոնց ստատուսը ՛գրանցված՛ է
                for (EventUsers eventUser : eventUsers) {
                    List<UserEventNotification> allByEventAndUserAndNotificationNumber = userEventNotificationRepository.findAllByEventAndUserAndNotificationNumber(event, eventUser.getUser(), REMINDER_NOTOFICATION_NUMBER);
                    if(allByEventAndUserAndNotificationNumber.size()==0){
                        userEventNotificationRepository.save(UserEventNotification.builder()
                                .user(eventUser.getUser())
                                .event(eventUser.getEvent())
                                .notificationNumber(5)
                                .readingStatus(false)
                                .build());}
                }
            }

        }
    }


//    @Scheduled(fixedDelay = 10800000 /*3 ժամ*/) //1ժամ =3600000 3ժամ=10800000
//    public void schedulerForNotificationsReminder() {
//
//        int month = ARMENIA_DATE_TIME.getMonthOfYear();
//        int day = ARMENIA_DATE_TIME.getDayOfMonth();
//        int year = ARMENIA_DATE_TIME.getYear();
//        int  hour = ARMENIA_DATE_TIME.getHourOfDay();
//        int minute = ARMENIA_DATE_TIME.getMinuteOfHour();
//
//
//        List<Event> events = eventRepository.findAllByEventStatus(EventStatus.Ընթացիկ);
//        for (Event event : events) {
//            String occurDate = event.getOccurDate();
//            String[] occurDateInfo = occurDate.split(" ");
//            String[] eventDate = occurDateInfo[0].split("-");
//            int eventYear = Integer.parseInt(eventDate[0]);
//            int eventMonth = 0;
//            if (eventDate[1].substring(0, 1).equals("0")) {
//                eventMonth = Integer.parseInt(eventDate[1].substring(1));
//            } else {
//                eventMonth = Integer.parseInt(eventDate[1]);
//
//            }
//            int eventDay = 0;
//            if (eventDate[2].substring(0, 1).equals("0")) {
//                eventDay = Integer.parseInt(eventDate[2].substring(1));
//            } else {
//                eventDay = Integer.parseInt(eventDate[2]);
//            }
//            int eventHour =0;
//            if(occurDateInfo[1].substring(0,1).equals("0")){
//                eventHour = Integer.parseInt(occurDateInfo[1].substring(1,2));
//            }else{
//                eventHour = Integer.parseInt(occurDateInfo[1].substring(0,2));
//            }
//            int eventMinute = 0;
//            if(occurDateInfo[1].split(":")[1].startsWith("0")){
//                eventMinute = Integer.parseInt(occurDateInfo[1].substring(4,5));
//            }else{
//                eventMinute = Integer.parseInt(occurDateInfo[1].substring(3,5));
//            }
//            if(year>=eventYear && month>=eventMonth && day>=eventDay && hour>=eventHour && minute>=eventMinute){
//                event.setEventStatus(EventStatus.Ավարտված);
//                eventRepository.save(event);
//            }
//
//            if (year == eventYear && month == eventMonth && day  == eventDay-1) {
//                List<EventUsers> eventUsers = eventUsersRepository.findAllByEventAndUserStatus(event, userEventStatusRepository.findAllById(1)); //այն eventUsers-ները որոնց ստատուսը ՛գրանցված՛ է
//                for (EventUsers eventUser : eventUsers) {
//                    List<UserEventNotification> allByEventAndUserAndNotificationNumber = userEventNotificationRepository.findAllByEventAndUserAndNotificationNumber(event, eventUser.getUser(), REMINDER_NOTOFICATION_NUMBER);
//                    if(allByEventAndUserAndNotificationNumber.size()==0){
//                    userEventNotificationRepository.save(UserEventNotification.builder()
//                            .user(eventUser.getUser())
//                            .event(eventUser.getEvent())
//                            .notificationNumber(5)
//                            .readingStatus(false)
//                            .build());}
//                }
//            }
//
//        }
//    }
}