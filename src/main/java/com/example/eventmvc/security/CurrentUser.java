package com.example.eventmvc.security;



import com.example.eventmvc.model.User;
import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public CurrentUser(User user) {
        super(user.getUsername(), user.getPassword(),user.getToken()==null,true,true,true, AuthorityUtils.createAuthorityList("user"));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
