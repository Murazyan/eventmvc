package com.example.eventmvc.security;




import com.example.eventmvc.model.User;
import com.example.eventmvc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findAllByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        }
        return new CurrentUser(user);
    }
}
