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
    public String toString() {
        return "Message{" +
                "event=" + event +
                ", toUser=" + toUser +
                ", text='" + text + '\'' +
                ", readingStatus=" + readingStatus +
                ", craterSendStatus=" + craterSendStatus +
                '}';
    }
}
