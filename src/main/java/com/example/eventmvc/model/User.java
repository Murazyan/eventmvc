package com.example.eventmvc.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
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
    private String username;
    @Column
    private String nickname;
    @Column
    private String password;
    @Column(name = "pic_url")
    private String picUrl;
    @Column(name = "other_info")
    private String otherInfo;
    @Column(name = "registration_date")
    private String registrationDate;
    @Column(name = "update_date")
    private Date updateDate;
    @Column
    private String token;
    @Column
    private boolean first_access_after_forgot;
//    @Column
//    private boolean verify;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "contacts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_user_id"))
    protected List<User> contactUser;
    @Column(name = "preferences_themes")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_preference",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "preference_id"))
    private List<PreferencesThemes> preferencesThemes;


}
