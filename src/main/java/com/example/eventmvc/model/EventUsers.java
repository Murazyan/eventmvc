package com.example.eventmvc.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventUsers that = (EventUsers) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {

        return Objects.hash(user, event);
    }
}
