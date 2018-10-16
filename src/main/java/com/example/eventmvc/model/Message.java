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
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Event event;
    @ManyToOne(cascade = CascadeType.REFRESH)
    private User toUser;
    @Column
    private String text;
    @Column(name = "creating_date")
    private String date;
    @Column(name = "reading_status")
    private boolean readingStatus;
    @Column(name = "creater_send_status")
    private boolean craterSendStatus;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(event, message.event) &&
                Objects.equals(toUser, message.toUser);
    }

    @Override
    public int hashCode() {

        return Objects.hash(event, toUser);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", event=" + event +
                ", toUser=" + toUser +
                ", text='" + text + '\'' +
                ", date='" + date + '\'' +
                ", readingStatus=" + readingStatus +
                ", craterSendStatus=" + craterSendStatus +
                '}';
    }
}
