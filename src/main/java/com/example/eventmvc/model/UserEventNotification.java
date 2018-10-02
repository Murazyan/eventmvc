package com.example.eventmvc.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "eventuser_notification")
public class UserEventNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column(name = "reading_status")
    private boolean readingStatus;
    @ManyToOne
    private EventUsers eventUsers;
    @Column(name = "notification_number")
    private int notificationNumber;
    @Column(name = "notification_Date")
    private String notificationDate;


}
