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
@Table(name = "event_users")
public class EventUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @ManyToOne
    private User user;
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Event event;
    @ManyToOne
    private UserEventStatus userStatus;
//    @ManyToOne
//    private UserNotification usernotification;


}
