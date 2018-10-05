package com.example.eventmvc.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NotificationDto {
    private String eventName;
    private int  notificationNumber;
    private String notificationDate;
    private String eventPicUrl;
//    private int notReadingNotificationCount;
}
