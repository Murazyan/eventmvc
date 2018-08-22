package com.example.eventmvc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String nickname;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String token;
    @Column(name = "pic_url")
    private String picUrl;
    @Column
    private String date;


}
