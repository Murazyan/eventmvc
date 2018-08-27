package com.example.eventmvc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
    @Column(name = "contact")
    @ManyToMany(fetch = FetchType.EAGER)
//    @ManyToMany
    @JoinTable(name = "contact",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_user_id"))
    protected List<User> contactUser;
}
